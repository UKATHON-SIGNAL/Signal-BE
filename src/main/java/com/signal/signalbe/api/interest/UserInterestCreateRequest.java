package com.signal.signalbe.api.interest;

import jakarta.validation.constraints.NotNull;

public record UserInterestCreateRequest(@NotNull Long topicId) {
}
