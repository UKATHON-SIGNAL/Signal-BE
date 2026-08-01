package com.signal.signalbe.api.interest;

import jakarta.validation.constraints.NotNull;

public record UserInterestCreateRequest(
        @NotNull(message = "추가할 관심 주제를 선택해주세요.") Long categoryId
) {
}
