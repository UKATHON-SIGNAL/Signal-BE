package com.signal.signalbe.domain.card;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardAttachmentRepository extends JpaRepository<CardAttachment, Long> {

    List<CardAttachment> findByCardId(Long cardId);
}
