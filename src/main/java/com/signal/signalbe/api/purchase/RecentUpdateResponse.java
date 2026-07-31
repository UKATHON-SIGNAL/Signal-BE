package com.signal.signalbe.api.purchase;

import com.signal.signalbe.domain.transaction.RecentUpdate;

import java.time.LocalDateTime;

public record RecentUpdateResponse(
        Long cardId,
        String title,
        String authorNickname,
        LocalDateTime evaluatedAt
) {
    public static RecentUpdateResponse from(RecentUpdate update) {
        return new RecentUpdateResponse(
                update.card().getId(),
                update.card().getTitle(),
                update.card().getAuthor().getNickname(),
                update.evaluatedAt()
        );
    }
}
