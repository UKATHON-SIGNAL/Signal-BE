package com.signal.signalbe.api.card;

import com.signal.signalbe.domain.card.CardSource;

public record SourceResponse(String url, String title) {
    public static SourceResponse from(CardSource source) {
        return new SourceResponse(source.getUrl(), source.getTitle());
    }
}
