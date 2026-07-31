package com.signal.signalbe.domain.transaction;

import com.signal.signalbe.domain.card.Card;
import com.signal.signalbe.domain.card.CardRepository;
import com.signal.signalbe.domain.user.User;
import com.signal.signalbe.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    @Transactional
    public Bookmark createBookmark(Long userId, Long cardId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. id=" + userId));
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 정보 카드입니다. id=" + cardId));

        if (bookmarkRepository.existsByUserIdAndCardId(userId, cardId)) {
            throw new IllegalStateException("이미 저장한 카드입니다.");
        }

        return bookmarkRepository.save(new Bookmark(user, card));
    }

    @Transactional
    public void deleteBookmark(Long userId, Long cardId) {
        bookmarkRepository.deleteByUserIdAndCardId(userId, cardId);
    }

    public List<Bookmark> getBookmarksByUser(Long userId) {
        return bookmarkRepository.findByUserId(userId);
    }
}
