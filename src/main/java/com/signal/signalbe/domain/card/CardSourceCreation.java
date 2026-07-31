package com.signal.signalbe.domain.card;

import java.time.LocalDateTime;

public record CardSourceCreation(String url, String title, String publisher, LocalDateTime sourcePublishedAt) {
}
