package com.signal.signalbe.domain.card;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardTopicRepository extends JpaRepository<CardTopic, Long> {

    List<CardTopic> findByCardId(Long cardId);

    List<CardTopic> findByTopicIdIn(List<Long> topicIds);
}
