package com.signal.signalbe.client.signalai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResolveRequest(
        String claim,
        @JsonProperty("success_condition") String successCondition,
        @JsonProperty("failure_condition") String failureCondition,
        @JsonProperty("actual_result") String actualResult,
        @JsonProperty("evidence_summary") String evidenceSummary
) {
}
