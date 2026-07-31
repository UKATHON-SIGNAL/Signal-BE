package com.signal.signalbe.client.signalai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.signal.signalbe.domain.verification.AiVerificationStatus;
import com.signal.signalbe.domain.verification.ReviewLevel;

public record VerifyResponse(
        AiVerificationStatus status,
        @JsonProperty("generated_title") String generatedTitle,
        @JsonProperty("generated_summary") String generatedSummary,
        @JsonProperty("recommended_price_min") Integer recommendedPriceMin,
        @JsonProperty("recommended_price_max") Integer recommendedPriceMax,
        @JsonProperty("evidence_relevance_level") ReviewLevel evidenceRelevanceLevel,
        @JsonProperty("evidence_relevance_comment") String evidenceRelevanceComment,
        @JsonProperty("missing_variable_level") ReviewLevel missingVariableLevel,
        @JsonProperty("missing_variable_comment") String missingVariableComment,
        @JsonProperty("counterargument_level") ReviewLevel counterargumentLevel,
        @JsonProperty("counterargument_comment") String counterargumentComment,
        @JsonProperty("overall_comment") String overallComment
) {
}
