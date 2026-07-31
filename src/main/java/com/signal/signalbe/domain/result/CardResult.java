package com.signal.signalbe.domain.result;

import com.signal.signalbe.domain.card.Card;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "card_results")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardResult extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false, unique = true)
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by_id", nullable = false)
    private User submittedBy;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String actualResult;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String evidenceSummary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResultStatus status;

    @Enumerated(EnumType.STRING)
    private ResultVerdict verdict;

    @Column(columnDefinition = "TEXT")
    private String aiReason;

    @Column(nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    private LocalDateTime evaluatedAt;

    public CardResult(Card card, User submittedBy, String actualResult, String evidenceSummary) {
        this.card = card;
        this.submittedBy = submittedBy;
        this.actualResult = actualResult;
        this.evidenceSummary = evidenceSummary;
        this.status = ResultStatus.SUBMITTED;
        this.submittedAt = LocalDateTime.now();
    }

    public void markAiReviewing() {
        this.status = ResultStatus.AI_REVIEWING;
    }

    public void applyEvaluation(ResultVerdict verdict, String aiReason, LocalDateTime evaluatedAt) {
        this.status = ResultStatus.EVALUATED;
        this.verdict = verdict;
        this.aiReason = aiReason;
        this.evaluatedAt = evaluatedAt;
    }

    public void markFailed() {
        this.status = ResultStatus.FAILED;
    }
}
