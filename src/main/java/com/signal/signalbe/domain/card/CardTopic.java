package com.signal.signalbe.domain.card;

import com.signal.signalbe.domain.category.Topic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "card_topics", uniqueConstraints = @UniqueConstraint(columnNames = {"card_id", "topic_id"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public CardTopic(Card card, Topic topic) {
        this.card = card;
        this.topic = topic;
        this.createdAt = LocalDateTime.now();
    }
}
