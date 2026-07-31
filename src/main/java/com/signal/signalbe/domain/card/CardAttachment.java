package com.signal.signalbe.domain.card;

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
@Table(name = "card_attachments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Column(nullable = false, length = 1000)
    private String fileUrl;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false, length = 50)
    private String fileType;

    @Column(nullable = false)
    private long fileSize;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public CardAttachment(Card card, String fileUrl, String originalFilename, String fileType, long fileSize) {
        this.card = card;
        this.fileUrl = fileUrl;
        this.originalFilename = originalFilename;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.createdAt = LocalDateTime.now();
    }
}
