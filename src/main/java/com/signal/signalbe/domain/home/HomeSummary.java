package com.signal.signalbe.domain.home;

import java.time.LocalDateTime;

public record HomeSummary(
        int purchasedCount,
        LocalDateTime lastPurchasedAt,
        int savedCount,
        int interestTopicCount,
        int publishedCount,
        int inReviewCount,
        int resultsConfirmedThisWeek
) {
}
