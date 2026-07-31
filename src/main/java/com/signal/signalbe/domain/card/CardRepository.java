package com.signal.signalbe.domain.card;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByStatus(CardStatus status);

    List<Card> findByAuthorId(Long authorId);

    List<Card> findByCategoryIdAndStatus(Long categoryId, CardStatus status);
}
