package com.signal.signalbe.api.purchase;

import com.signal.signalbe.domain.transaction.Purchase;

import java.time.LocalDateTime;

public record PurchaseResponse(
        Long id,
        Long buyerId,
        Long cardId,
        int purchasePrice,
        LocalDateTime purchasedAt,
        LocalDateTime resultCheckedAt
) {
    public static PurchaseResponse from(Purchase purchase) {
        return new PurchaseResponse(
                purchase.getId(),
                purchase.getBuyer().getId(),
                purchase.getCard().getId(),
                purchase.getPurchasePrice(),
                purchase.getPurchasedAt(),
                purchase.getResultCheckedAt()
        );
    }
}
