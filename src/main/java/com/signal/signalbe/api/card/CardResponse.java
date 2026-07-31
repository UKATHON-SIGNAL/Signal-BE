package com.signal.signalbe.api.card;

import com.signal.signalbe.domain.card.CardDetail;
import com.signal.signalbe.domain.card.CardStatus;
import com.signal.signalbe.domain.transaction.MyPurchaseStatus;
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
        LocalDateTime createdAt,
        boolean hasFullAccess,
        boolean bookmarked,
        MyPurchaseStatus resultStatus,
        LocalDateTime purchasedAt
) {
    public static CardResponse from(CardDetail detail) {
        var card = detail.card();
        var authorProfile = detail.authorProfile();
        var latestVerification = detail.latestVerification();

        boolean verified = authorProfile != null
                && authorProfile.getVerificationStatus() == CreatorVerificationStatus.VERIFIED;
        boolean hasFullAccess = detail.hasFullAccess();

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
                hasFullAccess ? card.getSuccessCondition() : null,
                hasFullAccess ? card.getFailureCondition() : null,
                hasFullAccess ? card.getEvidenceSummary() : null,
                card.getResultDueAt(),
                card.getSalePrice(),
                card.getStatus(),
                latestVerification != null ? latestVerification.getStatus() : null,
                latestVerification != null ? latestVerification.getRecommendedPriceMin() : null,
                latestVerification != null ? latestVerification.getRecommendedPriceMax() : null,
                detail.sources().size(),
                hasFullAccess ? detail.sources().stream().map(SourceResponse::from).toList() : List.of(),
                detail.purchaseCount(),
                card.getPublishedAt(),
                card.getCreatedAt(),
                hasFullAccess,
                detail.bookmarked(),
                detail.resultStatus(),
                detail.purchasedAt()
        );
    }
}
