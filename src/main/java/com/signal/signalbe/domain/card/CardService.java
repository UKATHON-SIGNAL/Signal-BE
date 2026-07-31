package com.signal.signalbe.domain.card;

import com.signal.signalbe.client.signalai.SignalAiClient;
import com.signal.signalbe.client.signalai.dto.SourceInput;
import com.signal.signalbe.client.signalai.dto.VerifyRequest;
import com.signal.signalbe.client.signalai.dto.VerifyResponse;
import com.signal.signalbe.domain.category.Category;
import com.signal.signalbe.domain.category.CategoryRepository;
import com.signal.signalbe.domain.category.Topic;
import com.signal.signalbe.domain.category.TopicRepository;
import com.signal.signalbe.domain.category.UserInterestRepository;
import com.signal.signalbe.domain.transaction.PurchaseRepository;
import com.signal.signalbe.domain.user.CreatorProfile;
import com.signal.signalbe.domain.user.CreatorProfileRepository;
import com.signal.signalbe.domain.user.CreatorProfileService;
import com.signal.signalbe.domain.user.User;
import com.signal.signalbe.domain.user.UserRepository;
import com.signal.signalbe.domain.verification.AiVerification;
import com.signal.signalbe.domain.verification.AiVerificationRepository;
import com.signal.signalbe.domain.verification.AiVerificationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final CardTopicRepository cardTopicRepository;
    private final CardSourceRepository cardSourceRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TopicRepository topicRepository;
    private final AiVerificationRepository aiVerificationRepository;
    private final CreatorProfileRepository creatorProfileRepository;
    private final CreatorProfileService creatorProfileService;
    private final UserInterestRepository userInterestRepository;
    private final PurchaseRepository purchaseRepository;
    private final SignalAiClient signalAiClient;

    @Transactional
    public Card createDraft(Long authorId, Long categoryId, String claim, String successCondition,
                             String failureCondition, String evidenceSummary, LocalDateTime resultDueAt,
                             List<Long> topicIds, List<CardSourceCreation> sources) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. id=" + authorId));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다. id=" + categoryId));

        Card card = new Card(author, category, claim, successCondition, failureCondition, evidenceSummary, resultDueAt);
        cardRepository.save(card);

        if (topicIds != null) {
            for (Long topicId : topicIds) {
                Topic topic = topicRepository.findById(topicId)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 토픽입니다. id=" + topicId));
                cardTopicRepository.save(new CardTopic(card, topic));
            }
        }

        for (CardSourceCreation source : sources) {
            cardSourceRepository.save(new CardSource(
                    card, source.url(), source.title(), source.publisher(), source.sourcePublishedAt()));
        }

        return card;
    }

    @Transactional
    public AiVerification requestAiReview(Long cardId) {
        Card card = getCard(cardId);
        card.startAiReview();

        List<CardSource> sources = cardSourceRepository.findByCardId(cardId);
        CreatorProfile authorProfile = creatorProfileRepository.findByUserId(card.getAuthor().getId()).orElse(null);
        long daysUntilResult = ChronoUnit.DAYS.between(LocalDateTime.now(), card.getResultDueAt());
        DuplicationCheck duplication = checkDuplication(card);
        VerifyResponse response = signalAiClient.verify(new VerifyRequest(
                card.getClaim(),
                card.getSuccessCondition(),
                card.getFailureCondition(),
                card.getEvidenceSummary(),
                card.getCategory().getName(),
                sources.stream().map(s -> new SourceInput(s.getUrl(), s.getTitle())).toList(),
                authorProfile != null ? authorProfile.getAverageScore() : null,
                authorProfile != null ? authorProfile.getTotalEvaluatedCount() : 0,
                creatorProfileService.getSourceReliability(card.getAuthor().getId()),
                daysUntilResult,
                duplication.score()
        ));

        AiVerification verification = new AiVerification(card);
        aiVerificationRepository.save(verification);

        boolean passed = response.status() == AiVerificationStatus.PASSED;
        verification.applyResult(
                passed,
                response.generatedTitle(),
                response.generatedSummary(),
                response.recommendedPriceMin(),
                response.recommendedPriceMax(),
                response.evidenceRelevanceLevel(),
                response.evidenceRelevanceComment(),
                response.missingVariableLevel(),
                response.missingVariableComment(),
                response.counterargumentLevel(),
                response.counterargumentComment(),
                duplication.score(),
                duplication.mostSimilarCardId(),
                duplication.mostSimilarCardTitle(),
                response.overallComment(),
                LocalDateTime.now()
        );

        if (passed) {
            card.completeAiReview(response.generatedTitle(), response.generatedSummary());
        } else {
            card.failAiReview();
        }

        return verification;
    }

    @Transactional
    public Card setPrice(Long cardId, int salePrice) {
        Card card = getCard(cardId);
        if (card.getStatus() != CardStatus.PRICE_SETTING) {
            throw new IllegalStateException("가격 설정 단계가 아닙니다. 현재 상태=" + card.getStatus());
        }
        card.setSalePrice(salePrice);
        return card;
    }

    @Transactional
    public Card publish(Long cardId) {
        Card card = getCard(cardId);
        if (card.getStatus() != CardStatus.PRICE_SETTING || card.getSalePrice() == null) {
            throw new IllegalStateException("발행할 수 없는 상태입니다. 현재 상태=" + card.getStatus());
        }
        card.publish(LocalDateTime.now());

        CreatorProfile profile = creatorProfileRepository.findByUserId(card.getAuthor().getId())
                .orElseGet(() -> creatorProfileRepository.save(new CreatorProfile(card.getAuthor())));
        profile.increasePublishedCount();

        return card;
    }

    public Card getCard(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 정보 카드입니다. id=" + cardId));
    }

    public List<Card> getCards(CardStatus status) {
        return status == null ? cardRepository.findAll() : cardRepository.findByStatus(status);
    }

    public CardDetail getCardDetail(Long cardId) {
        return buildDetail(getCard(cardId));
    }

    public CardDetail getCardDetail(Long cardId, Long viewerId) {
        return buildDetail(getCard(cardId), viewerId);
    }

    public List<CardDetail> getCardDetails(CardSearchCriteria criteria) {
        return getCardDetails(criteria, null);
    }

    public List<CardDetail> getCardDetails(CardSearchCriteria criteria, Long viewerId) {
        List<CardDetail> details = getCards(criteria.status()).stream()
                .map(card -> buildDetail(card, viewerId))
                .toList();

        List<CardDetail> filtered = details.stream()
                .filter(d -> criteria.authorId() == null || d.card().getAuthor().getId().equals(criteria.authorId()))
                .filter(d -> criteria.priceMin() == null
                        || (d.card().getSalePrice() != null && d.card().getSalePrice() >= criteria.priceMin()))
                .filter(d -> criteria.priceMax() == null
                        || (d.card().getSalePrice() != null && d.card().getSalePrice() <= criteria.priceMax()))
                .filter(d -> criteria.resultDueFrom() == null
                        || !d.card().getResultDueAt().isBefore(criteria.resultDueFrom()))
                .filter(d -> criteria.resultDueTo() == null
                        || !d.card().getResultDueAt().isAfter(criteria.resultDueTo()))
                .filter(d -> criteria.aiVerificationStatus() == null
                        || (d.latestVerification() != null
                        && d.latestVerification().getStatus() == criteria.aiVerificationStatus()))
                .filter(d -> criteria.keyword() == null || criteria.keyword().isBlank()
                        || containsIgnoreCase(d.card().getClaim(), criteria.keyword())
                        || containsIgnoreCase(d.card().getTitle(), criteria.keyword())
                        || containsIgnoreCase(d.card().getAuthor().getNickname(), criteria.keyword()))
                .toList();

        Comparator<CardDetail> comparator = switch (criteria.sort() == null ? CardSort.LATEST : criteria.sort()) {
            case PRICE_ASC -> Comparator.comparing(
                    (CardDetail d) -> d.card().getSalePrice(), Comparator.nullsLast(Comparator.naturalOrder()));
            case PRICE_DESC -> Comparator.comparing(
                    (CardDetail d) -> d.card().getSalePrice(), Comparator.nullsLast(Comparator.naturalOrder())).reversed();
            case RESULT_DUE_SOON -> Comparator.comparing(d -> d.card().getResultDueAt());
            case LATEST -> Comparator.comparing((CardDetail d) -> d.card().getCreatedAt()).reversed();
        };

        return filtered.stream().sorted(comparator).toList();
    }

    public List<CardDetail> getRecommendedCards(Long userId, int limit) {
        List<Long> topicIds = userInterestRepository.findByUserId(userId).stream()
                .map(interest -> interest.getTopic().getId())
                .toList();

        List<Card> matched = topicIds.isEmpty() ? List.of() :
                cardTopicRepository.findByTopicIdIn(topicIds).stream()
                        .map(CardTopic::getCard)
                        .filter(card -> card.getStatus() == CardStatus.PUBLISHED)
                        .distinct()
                        .toList();

        List<Card> result = new ArrayList<>(matched.stream().limit(limit).toList());

        if (result.size() < limit) {
            List<Card> fallback = cardRepository.findByStatus(CardStatus.PUBLISHED).stream()
                    .filter(card -> !result.contains(card))
                    .sorted(Comparator.comparing(Card::getPublishedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                    .toList();
            for (Card card : fallback) {
                if (result.size() >= limit) {
                    break;
                }
                result.add(card);
            }
        }

        return result.stream().map(card -> buildDetail(card, userId)).toList();
    }

    private static boolean containsIgnoreCase(String source, String keyword) {
        return source != null && source.toLowerCase().contains(keyword.toLowerCase());
    }

    private DuplicationCheck checkDuplication(Card card) {
        Card mostSimilar = null;
        double maxSimilarity = 0.0;
        for (Card other : cardRepository.findByStatus(CardStatus.PUBLISHED)) {
            if (other.getId().equals(card.getId())) {
                continue;
            }
            double similarity = DuplicationScorer.similarity(card.getClaim(), other.getClaim());
            if (similarity > maxSimilarity) {
                maxSimilarity = similarity;
                mostSimilar = other;
            }
        }
        BigDecimal score = BigDecimal.valueOf(Math.round(maxSimilarity * 100));
        return new DuplicationCheck(
                score,
                mostSimilar != null ? mostSimilar.getId() : null,
                mostSimilar != null ? mostSimilar.getTitle() : null
        );
    }

    private CardDetail buildDetail(Card card) {
        return buildDetail(card, true);
    }

    private CardDetail buildDetail(Card card, Long viewerId) {
        boolean hasFullAccess = viewerId != null
                && (viewerId.equals(card.getAuthor().getId())
                || purchaseRepository.existsByBuyerIdAndCardId(viewerId, card.getId()));
        return buildDetail(card, hasFullAccess);
    }

    private CardDetail buildDetail(Card card, boolean hasFullAccess) {
        CreatorProfile authorProfile = creatorProfileRepository.findByUserId(card.getAuthor().getId()).orElse(null);
        AiVerification latestVerification = aiVerificationRepository
                .findByCardIdOrderByCreatedAtDesc(card.getId())
                .stream()
                .findFirst()
                .orElse(null);
        List<CardSource> sources = cardSourceRepository.findByCardId(card.getId());
        int purchaseCount = purchaseRepository.findByCardId(card.getId()).size();
        return new CardDetail(card, authorProfile, latestVerification, sources, purchaseCount, hasFullAccess);
    }
}
