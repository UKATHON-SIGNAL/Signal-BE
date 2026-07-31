package com.signal.signalbe.domain.card;

import com.signal.signalbe.domain.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "card_sources")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardSource extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Column(nullable = false, length = 1000)
    private String url;

    @Column(length = 500)
    private String title;

    @Column(length = 255)
    private String publisher;

    private LocalDateTime sourcePublishedAt;

    public CardSource(Card card, String url, String title, String publisher, LocalDateTime sourcePublishedAt) {
        this.card = card;
        this.url = url;
        this.title = title;
        this.publisher = publisher;
        this.sourcePublishedAt = sourcePublishedAt;
    }
}
