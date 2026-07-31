package com.signal.signalbe.api.home;

import com.signal.signalbe.domain.home.HomeSummary;

import java.time.LocalDateTime;

public record HomeSummaryResponse(
        int purchasedCount,
        LocalDateTime lastPurchasedAt,
        int savedCount,
        int interestTopicCount,
        int publishedCount,
        int inReviewCount,
        int resultsConfirmedThisWeek
) {
    public static HomeSummaryResponse from(HomeSummary summary) {
        return new HomeSummaryResponse(
                summary.purchasedCount(),
                summary.lastPurchasedAt(),
                summary.savedCount(),
                summary.interestTopicCount(),
                summary.publishedCount(),
                summary.inReviewCount(),
                summary.resultsConfirmedThisWeek()
        );
    }
}
