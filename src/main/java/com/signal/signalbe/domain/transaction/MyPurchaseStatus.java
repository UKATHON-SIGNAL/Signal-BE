package com.signal.signalbe.domain.transaction;

import com.signal.signalbe.domain.result.CardResult;
import com.signal.signalbe.domain.result.ResultStatus;

public enum MyPurchaseStatus {
    IN_PROGRESS,
    PENDING_CONFIRMATION,
    CONFIRMED,
    SAVED;

    public static MyPurchaseStatus resolve(Purchase purchase, CardResult result) {
        if (result == null || result.getStatus() != ResultStatus.EVALUATED) {
            return IN_PROGRESS;
        }
        return purchase.getResultCheckedAt() != null ? CONFIRMED : PENDING_CONFIRMATION;
    }
}
