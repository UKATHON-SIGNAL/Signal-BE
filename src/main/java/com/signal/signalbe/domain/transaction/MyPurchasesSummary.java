package com.signal.signalbe.domain.transaction;

public record MyPurchasesSummary(
        int purchasedCount, long totalPurchaseAmount, int pendingConfirmationCount, int savedCount) {
}
