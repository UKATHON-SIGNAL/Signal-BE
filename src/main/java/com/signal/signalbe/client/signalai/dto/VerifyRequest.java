package com.signal.signalbe.client.signalai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record VerifyRequest(
        String claim,
        @JsonProperty("success_condition") String successCondition,
        @JsonProperty("failure_condition") String failureCondition,
        @JsonProperty("evidence_summary") String evidenceSummary,
        String category,
        List<SourceInput> sources
) {
}
