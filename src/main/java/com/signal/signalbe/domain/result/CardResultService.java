package com.signal.signalbe.domain.result;

import com.signal.signalbe.client.signalai.SignalAiClient;
import com.signal.signalbe.client.signalai.dto.ResolveRequest;
import com.signal.signalbe.client.signalai.dto.ResolveResponse;
import com.signal.signalbe.domain.card.Card;
import com.signal.signalbe.domain.card.CardRepository;
import com.signal.signalbe.domain.card.CardStatus;
import com.signal.signalbe.domain.user.CreatorProfile;
import com.signal.signalbe.domain.user.CreatorProfileRepository;
import com.signal.signalbe.domain.user.User;
import com.signal.signalbe.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardResultService {

    private final CardResultRepository cardResultRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CreatorProfileRepository creatorProfileRepository;
    private final SignalAiClient signalAiClient;

    @Transactional
    public CardResult submitResult(Long cardId, Long submittedById, String actualResult, String evidenceSummary) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 정보 카드입니다. id=" + cardId));
        if (card.getStatus() != CardStatus.PUBLISHED) {
            throw new IllegalStateException("발행된 카드만 결과를 제출할 수 있습니다. 현재 상태=" + card.getStatus());
        }
        if (cardResultRepository.findByCardId(cardId).isPresent()) {
            throw new IllegalStateException("이미 결과가 제출된 카드입니다.");
        }
        User submittedBy = userRepository.findById(submittedById)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. id=" + submittedById));

        card.markResultPending();
        card.startResultReview();

        CardResult cardResult = new CardResult(card, submittedBy, actualResult, evidenceSummary);
        cardResultRepository.save(cardResult);

        ResolveResponse response = signalAiClient.resolve(new ResolveRequest(
                card.getClaim(), card.getSuccessCondition(), card.getFailureCondition(),
                actualResult, evidenceSummary));

        cardResult.applyEvaluation(response.verdict(), response.aiReason(), LocalDateTime.now());
        card.complete();

        updateCreatorPerformance(card.getAuthor().getId(), response.verdict());

        return cardResult;
    }

    private void updateCreatorPerformance(Long authorId, ResultVerdict verdict) {
        CreatorProfile profile = creatorProfileRepository.findByUserId(authorId)
                .orElseGet(() -> {
                    User author = userRepository.findById(authorId)
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. id=" + authorId));
                    return creatorProfileRepository.save(new CreatorProfile(author));
                });
        profile.applyEvaluatedResult(verdict);
    }

    public CardResult getResult(Long cardId) {
        return cardResultRepository.findByCardId(cardId)
                .orElseThrow(() -> new IllegalArgumentException("제출된 결과가 없습니다. cardId=" + cardId));
    }
}
