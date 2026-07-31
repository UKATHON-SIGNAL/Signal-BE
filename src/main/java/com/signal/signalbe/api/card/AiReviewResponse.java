package com.signal.signalbe.api.card;

import com.signal.signalbe.domain.verification.AiVerification;
import com.signal.signalbe.domain.verification.AiVerificationStatus;
import com.signal.signalbe.domain.verification.ReviewLevel;

import java.math.BigDecimal;

public record AiReviewResponse(
        AiVerificationStatus status,
        String generatedTitle,
        String generatedSummary,
        Integer recommendedPriceMin,
        Integer recommendedPriceMax,
        ReviewLevel evidenceRelevanceLevel,
        String evidenceRelevanceComment,
        ReviewLevel missingVariableLevel,
        String missingVariableComment,
        ReviewLevel counterargumentLevel,
        String counterargumentComment,
        BigDecimal duplicationScore,
        Long mostSimilarCardId,
        String mostSimilarCardTitle,
        String overallComment
) {
    public static AiReviewResponse from(AiVerification verification) {
        return new AiReviewResponse(
                verification.getStatus(),
                verification.getGeneratedTitle(),
                verification.getGeneratedSummary(),
                verification.getRecommendedPriceMin(),
                verification.getRecommendedPriceMax(),
                verification.getEvidenceRelevanceLevel(),
                verification.getEvidenceRelevanceComment(),
                verification.getMissingVariableLevel(),
                verification.getMissingVariableComment(),
                verification.getCounterargumentLevel(),
                verification.getCounterargumentComment(),
                verification.getDuplicationScore(),
                verification.getMostSimilarCardId(),
                verification.getMostSimilarCardTitle(),
                verification.getOverallComment()
        );
    }
}
