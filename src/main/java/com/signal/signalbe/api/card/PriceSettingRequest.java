package com.signal.signalbe.api.card;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PriceSettingRequest(@NotNull @Min(0) Integer salePrice) {
}
