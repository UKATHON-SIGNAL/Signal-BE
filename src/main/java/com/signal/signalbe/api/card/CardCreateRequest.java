package com.signal.signalbe.api.card;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CardCreateRequest(
        @NotNull Long authorId,
        @NotNull Long categoryId,
        @NotBlank String claim,
        @NotBlank String successCondition,
        @NotBlank String failureCondition,
        @NotBlank String evidenceSummary,
        @NotBlank String evaluationMetric,
        @NotNull BigDecimal fullHitThreshold,
        @NotNull BigDecimal partialHitThreshold,
        @NotNull @Future LocalDateTime resultDueAt,
        List<Long> topicIds,
        @NotEmpty List<@Valid SourceRequest> sources
) {
}
