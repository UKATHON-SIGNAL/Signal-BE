package com.signal.signalbe.api.card;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CardCreateRequest(
        @NotNull Long authorId,
        @NotNull Long categoryId,
        @NotBlank String claim,
        @NotBlank String successCondition,
        @NotBlank String failureCondition,
        @NotBlank String evidenceSummary,
        @NotNull @Future LocalDateTime resultDueAt,
        @NotEmpty List<@Valid SourceRequest> sources
) {
}
