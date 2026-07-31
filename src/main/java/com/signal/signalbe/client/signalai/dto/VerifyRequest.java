package com.signal.signalbe.client.signalai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public record VerifyRequest(
        String claim,
        @JsonProperty("success_condition") String successCondition,
        @JsonProperty("failure_condition") String failureCondition,
        @JsonProperty("evidence_summary") String evidenceSummary,
        String category,
        List<SourceInput> sources,
        @JsonProperty("creator_average_score") BigDecimal creatorAverageScore,
        @JsonProperty("creator_evaluated_count") int creatorEvaluatedCount,
        @JsonProperty("creator_source_reliability") BigDecimal creatorSourceReliability,
        @JsonProperty("days_until_result") Long daysUntilResult
) {
}
