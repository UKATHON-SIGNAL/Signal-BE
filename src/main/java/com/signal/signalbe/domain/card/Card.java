package com.signal.signalbe.domain.card;

import com.signal.signalbe.domain.category.Category;
import com.signal.signalbe.domain.common.BaseTimeEntity;
import com.signal.signalbe.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "cards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String claim;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String successCondition;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String failureCondition;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String evidenceSummary;

    @Column(nullable = false)
    private LocalDateTime resultDueAt;

    private Integer salePrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatus status;

    private LocalDateTime publishedAt;

    public Card(User author, Category category, String claim, String successCondition,
                String failureCondition, String evidenceSummary, LocalDateTime resultDueAt) {
        this.author = author;
        this.category = category;
        this.claim = claim;
        this.successCondition = successCondition;
        this.failureCondition = failureCondition;
        this.evidenceSummary = evidenceSummary;
        this.resultDueAt = resultDueAt;
        this.status = CardStatus.DRAFT;
    }

    public void startAiReview() {
        this.status = CardStatus.AI_REVIEWING;
    }

    public void completeAiReview(String generatedTitle, String generatedSummary) {
        this.title = generatedTitle;
        this.summary = generatedSummary;
        this.status = CardStatus.PRICE_SETTING;
    }

    public void failAiReview() {
        this.status = CardStatus.AI_REVIEW_FAILED;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public void publish(LocalDateTime publishedAt) {
        this.status = CardStatus.PUBLISHED;
        this.publishedAt = publishedAt;
    }

    public void markResultPending() {
        this.status = CardStatus.RESULT_PENDING;
    }

    public void startResultReview() {
        this.status = CardStatus.RESULT_REVIEWING;
    }

    public void complete() {
        this.status = CardStatus.COMPLETED;
    }

    public void reject() {
        this.status = CardStatus.REJECTED;
    }
}
