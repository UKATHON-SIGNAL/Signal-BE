package com.signal.signalbe.api.bookmark;

import jakarta.validation.constraints.NotNull;

public record BookmarkCreateRequest(@NotNull Long userId, @NotNull Long cardId) {
}
