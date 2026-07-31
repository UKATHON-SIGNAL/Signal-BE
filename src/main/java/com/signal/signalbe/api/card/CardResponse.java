package com.signal.signalbe.api.card;

import com.signal.signalbe.domain.card.CardDetail;
import com.signal.signalbe.domain.card.CardStatus;
import com.signal.signalbe.domain.user.CreatorVerificationStatus;
import com.signal.signalbe.domain.verification.AiVerificationStatus;

import java.time.LocalDateTime;
import java.util.List;

public record CardResponse(
        Long id,
        Long authorId,
        String authorNickname,
        boolean authorVerified,
        Long categoryId,
        String categoryName,
        String title,
        String summary,
        String claim,
        String successCondition,
        String failureCondition,
        String evidenceSummary,
        LocalDateTime resultDueAt,
        Integer salePrice,
        CardStatus status,
        AiVerificationStatus aiVerificationStatus,
        Integer recommendedPriceMin,
        Integer recommendedPriceMax,
        int sourceCount,
        List<SourceResponse> sources,
        int purchaseCount,
        LocalDateTime publishedAt,
        LocalDateTime createdAt
) {
    public static CardResponse from(CardDetail detail) {
        var card = detail.card();
        var authorProfile = detail.authorProfile();
        var latestVerification = detail.latestVerification();

        boolean verified = authorProfile != null
                && authorProfile.getVerificationStatus() == CreatorVerificationStatus.VERIFIED;

        return new CardResponse(
                card.getId(),
                card.getAuthor().getId(),
                card.getAuthor().getNickname(),
                verified,
                card.getCategory().getId(),
                card.getCategory().getName(),
                card.getTitle(),
                card.getSummary(),
                card.getClaim(),
                card.getSuccessCondition(),
                card.getFailureCondition(),
                card.getEvidenceSummary(),
                card.getResultDueAt(),
                card.getSalePrice(),
                card.getStatus(),
                latestVerification != null ? latestVerification.getStatus() : null,
                latestVerification != null ? latestVerification.getRecommendedPriceMin() : null,
                latestVerification != null ? latestVerification.getRecommendedPriceMax() : null,
                detail.sources().size(),
                detail.sources().stream().map(SourceResponse::from).toList(),
                detail.purchaseCount(),
                card.getPublishedAt(),
                card.getCreatedAt()
        );
    }
}
