package com.signal.signalbe.domain.card;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardSourceRepository extends JpaRepository<CardSource, Long> {

    List<CardSource> findByCardId(Long cardId);
}
