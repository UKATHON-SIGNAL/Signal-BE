package com.signal.signalbe.domain.result;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardResultRepository extends JpaRepository<CardResult, Long> {

    Optional<CardResult> findByCardId(Long cardId);
}
