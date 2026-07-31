package com.signal.signalbe.api.card;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record SourceRequest(
        @NotBlank String url,
        String title,
        String publisher,
        LocalDateTime sourcePublishedAt
) {
}
