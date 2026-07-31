package com.signal.signalbe.api.result;

import com.signal.signalbe.domain.result.CardResult;
import com.signal.signalbe.domain.result.ResultStatus;
import com.signal.signalbe.domain.result.ResultVerdict;

import java.time.LocalDateTime;

public record CardResultResponse(
        Long id,
        Long cardId,
        Long submittedById,
        String actualResult,
        String evidenceSummary,
        ResultStatus status,
        ResultVerdict verdict,
        String aiReason,
        LocalDateTime submittedAt,
        LocalDateTime evaluatedAt
) {
    public static CardResultResponse from(CardResult cardResult) {
        return new CardResultResponse(
                cardResult.getId(),
                cardResult.getCard().getId(),
                cardResult.getSubmittedBy().getId(),
                cardResult.getActualResult(),
                cardResult.getEvidenceSummary(),
                cardResult.getStatus(),
                cardResult.getVerdict(),
                cardResult.getAiReason(),
                cardResult.getSubmittedAt(),
                cardResult.getEvaluatedAt()
        );
    }
}
