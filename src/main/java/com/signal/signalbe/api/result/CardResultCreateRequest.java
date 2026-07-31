package com.signal.signalbe.api.result;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CardResultCreateRequest(
        @NotNull Long submittedById,
        @NotBlank String actualResult,
        @NotBlank String evidenceSummary
) {
}
