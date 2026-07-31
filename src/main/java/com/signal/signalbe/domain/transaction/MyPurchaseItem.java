package com.signal.signalbe.domain.transaction;

import com.signal.signalbe.domain.card.Card;
import com.signal.signalbe.domain.verification.AiVerificationStatus;

import java.time.LocalDateTime;

public record MyPurchaseItem(
        Purchase purchase, Bookmark bookmark, Card card, MyPurchaseStatus status, boolean bookmarked,
        AiVerificationStatus aiVerificationStatus) {

    public LocalDateTime sortDate() {
        return purchase != null ? purchase.getPurchasedAt() : bookmark.getCreatedAt();
    }
}
