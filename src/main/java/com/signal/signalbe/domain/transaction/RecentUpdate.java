package com.signal.signalbe.domain.transaction;

import com.signal.signalbe.domain.card.Card;

import java.time.LocalDateTime;

public record RecentUpdate(Card card, LocalDateTime evaluatedAt) {
}
