package com.signal.signalbe.client.signalai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ResolveRequest(
        String claim,
        @JsonProperty("success_condition") String successCondition,
        @JsonProperty("failure_condition") String failureCondition,
        @JsonProperty("actual_result") String actualResult,
        @JsonProperty("evidence_summary") String evidenceSummary,
        @JsonProperty("evaluation_metric") String evaluationMetric,
        @JsonProperty("full_hit_threshold") BigDecimal fullHitThreshold,
        @JsonProperty("partial_hit_threshold") BigDecimal partialHitThreshold
) {
}
