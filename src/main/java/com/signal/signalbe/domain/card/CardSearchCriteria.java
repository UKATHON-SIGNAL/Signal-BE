package com.signal.signalbe.domain.card;

import com.signal.signalbe.domain.verification.AiVerificationStatus;

import java.time.LocalDateTime;

public record CardSearchCriteria(
        CardStatus status,
        Long authorId,
        Integer priceMin,
        Integer priceMax,
        LocalDateTime resultDueFrom,
        LocalDateTime resultDueTo,
        AiVerificationStatus aiVerificationStatus,
        String keyword,
        CardSort sort
) {
}
