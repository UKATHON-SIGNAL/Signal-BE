package com.signal.signalbe.domain.card;

import com.signal.signalbe.client.signalai.SignalAiClient;
import com.signal.signalbe.client.signalai.dto.SourceInput;
import com.signal.signalbe.client.signalai.dto.VerifyRequest;
import com.signal.signalbe.client.signalai.dto.VerifyResponse;
import com.signal.signalbe.domain.category.Category;
import com.signal.signalbe.domain.category.CategoryRepository;
import com.signal.signalbe.domain.category.Topic;
import com.signal.signalbe.domain.category.TopicRepository;
import com.signal.signalbe.domain.user.CreatorProfile;
import com.signal.signalbe.domain.user.CreatorProfileRepository;
import com.signal.signalbe.domain.user.User;
import com.signal.signalbe.domain.user.UserRepository;
import com.signal.signalbe.domain.verification.AiVerification;
import com.signal.signalbe.domain.verification.AiVerificationRepository;
import com.signal.signalbe.domain.verification.AiVerificationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        VerifyResponse response = signalAiClient.verify(new VerifyRequest(
                card.getClaim(),
                card.getSuccessCondition(),
                card.getFailureCondition(),
                card.getEvidenceSummary(),
                card.getCategory().getName(),
                sources.stream().map(s -> new SourceInput(s.getUrl(), s.getTitle())).toList()
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
                response.duplicationScore(),
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
}
