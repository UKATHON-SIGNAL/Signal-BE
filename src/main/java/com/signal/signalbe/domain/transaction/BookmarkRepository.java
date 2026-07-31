package com.signal.signalbe.domain.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findByUserId(Long userId);

    boolean existsByUserIdAndCardId(Long userId, Long cardId);

    void deleteByUserIdAndCardId(Long userId, Long cardId);
}
