package com.signal.signalbe.api.bookmark;

import com.signal.signalbe.domain.transaction.Bookmark;

import java.time.LocalDateTime;

public record BookmarkResponse(Long id, Long userId, Long cardId, LocalDateTime createdAt) {
    public static BookmarkResponse from(Bookmark bookmark) {
        return new BookmarkResponse(
                bookmark.getId(),
                bookmark.getUser().getId(),
                bookmark.getCard().getId(),
                bookmark.getCreatedAt()
        );
    }
}
