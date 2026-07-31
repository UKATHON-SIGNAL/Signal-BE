package com.signal.signalbe.domain.verification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiVerificationRepository extends JpaRepository<AiVerification, Long> {

    List<AiVerification> findByCardIdOrderByCreatedAtDesc(Long cardId);
}
