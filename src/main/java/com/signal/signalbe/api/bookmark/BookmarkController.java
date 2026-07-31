package com.signal.signalbe.api.bookmark;

import com.signal.signalbe.domain.transaction.Bookmark;
import com.signal.signalbe.domain.transaction.BookmarkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<BookmarkResponse> createBookmark(@Valid @RequestBody BookmarkCreateRequest request) {
        Bookmark bookmark = bookmarkService.createBookmark(request.userId(), request.cardId());
        return ResponseEntity.status(HttpStatus.CREATED).body(BookmarkResponse.from(bookmark));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBookmark(@RequestParam Long userId, @RequestParam Long cardId) {
        bookmarkService.deleteBookmark(userId, cardId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<BookmarkResponse> getBookmarks(@RequestParam Long userId) {
        return bookmarkService.getBookmarksByUser(userId).stream().map(BookmarkResponse::from).toList();
    }
}
