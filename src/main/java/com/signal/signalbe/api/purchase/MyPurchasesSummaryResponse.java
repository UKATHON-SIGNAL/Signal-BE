package com.signal.signalbe.api.purchase;

import com.signal.signalbe.domain.transaction.MyPurchasesSummary;

public record MyPurchasesSummaryResponse(
        int purchasedCount, long totalPurchaseAmount, int pendingConfirmationCount, int savedCount,
        int interestTopicCount
) {
    public static MyPurchasesSummaryResponse from(MyPurchasesSummary summary) {
        return new MyPurchasesSummaryResponse(
                summary.purchasedCount(),
                summary.totalPurchaseAmount(),
                summary.pendingConfirmationCount(),
                summary.savedCount(),
                summary.interestTopicCount()
        );
    }
}
