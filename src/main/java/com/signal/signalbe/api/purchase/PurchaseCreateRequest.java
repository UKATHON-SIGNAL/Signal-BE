package com.signal.signalbe.api.purchase;

import jakarta.validation.constraints.NotNull;

public record PurchaseCreateRequest(@NotNull Long buyerId, @NotNull Long cardId) {
}
