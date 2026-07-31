package com.signal.signalbe.api.card;

import com.signal.signalbe.domain.card.Card;
import com.signal.signalbe.domain.card.CardStatus;

import java.time.LocalDateTime;

public record CardResponse(
        Long id,
        Long authorId,
        Long categoryId,
        String title,
        String summary,
        String claim,
        String successCondition,
        String failureCondition,
        String evidenceSummary,
        LocalDateTime resultDueAt,
        Integer salePrice,
        CardStatus status,
        LocalDateTime publishedAt,
        LocalDateTime createdAt
) {
    public static CardResponse from(Card card) {
        return new CardResponse(
                card.getId(),
                card.getAuthor().getId(),
                card.getCategory().getId(),
                card.getTitle(),
                card.getSummary(),
                card.getClaim(),
                card.getSuccessCondition(),
                card.getFailureCondition(),
                card.getEvidenceSummary(),
                card.getResultDueAt(),
                card.getSalePrice(),
                card.getStatus(),
                card.getPublishedAt(),
                card.getCreatedAt()
        );
    }
}
