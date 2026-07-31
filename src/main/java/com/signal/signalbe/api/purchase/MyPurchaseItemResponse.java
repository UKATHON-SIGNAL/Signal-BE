package com.signal.signalbe.api.purchase;

import com.signal.signalbe.domain.transaction.MyPurchaseItem;
import com.signal.signalbe.domain.transaction.MyPurchaseStatus;

import java.time.LocalDateTime;

public record MyPurchaseItemResponse(
        Long cardId,
        String categoryName,
        String title,
        Integer purchasePrice,
        Integer salePrice,
        LocalDateTime date,
        MyPurchaseStatus status,
        boolean bookmarked
) {
    public static MyPurchaseItemResponse from(MyPurchaseItem item) {
        var card = item.card();
        return new MyPurchaseItemResponse(
                card.getId(),
                card.getCategory().getName(),
                card.getTitle(),
                item.purchase() != null ? item.purchase().getPurchasePrice() : null,
                card.getSalePrice(),
                item.sortDate(),
                item.status(),
                item.bookmarked()
        );
    }
}
