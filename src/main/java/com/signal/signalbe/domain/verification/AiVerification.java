package com.signal.signalbe.domain.verification;

import com.signal.signalbe.domain.card.Card;
import com.signal.signalbe.domain.common.BaseTimeEntity;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "ai_verifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiVerification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AiVerificationStatus status;

    private String generatedTitle;

    @Column(columnDefinition = "TEXT")
    private String generatedSummary;

    private Integer recommendedPriceMin;

    private Integer recommendedPriceMax;

    @Enumerated(EnumType.STRING)
    private ReviewLevel evidenceRelevanceLevel;

    @Column(columnDefinition = "TEXT")
    private String evidenceRelevanceComment;

    @Enumerated(EnumType.STRING)
    private ReviewLevel missingVariableLevel;

    @Column(columnDefinition = "TEXT")
    private String missingVariableComment;

    @Enumerated(EnumType.STRING)
    private ReviewLevel counterargumentLevel;

    @Column(columnDefinition = "TEXT")
    private String counterargumentComment;

    private BigDecimal duplicationScore;

    private Long mostSimilarCardId;

    private String mostSimilarCardTitle;

    @Column(columnDefinition = "TEXT")
    private String overallComment;

    private LocalDateTime reviewedAt;

    public AiVerification(Card card) {
        this.card = card;
        this.status = AiVerificationStatus.PENDING;
    }

    public void markInProgress() {
        this.status = AiVerificationStatus.IN_PROGRESS;
    }

    public void applyResult(
            boolean passed,
            String generatedTitle,
            String generatedSummary,
            Integer recommendedPriceMin,
            Integer recommendedPriceMax,
            ReviewLevel evidenceRelevanceLevel,
            String evidenceRelevanceComment,
            ReviewLevel missingVariableLevel,
            String missingVariableComment,
            ReviewLevel counterargumentLevel,
            String counterargumentComment,
            BigDecimal duplicationScore,
            Long mostSimilarCardId,
            String mostSimilarCardTitle,
            String overallComment,
            LocalDateTime reviewedAt
    ) {
        this.status = passed ? AiVerificationStatus.PASSED : AiVerificationStatus.FAILED;
        this.generatedTitle = generatedTitle;
        this.generatedSummary = generatedSummary;
        this.recommendedPriceMin = recommendedPriceMin;
        this.recommendedPriceMax = recommendedPriceMax;
        this.evidenceRelevanceLevel = evidenceRelevanceLevel;
        this.evidenceRelevanceComment = evidenceRelevanceComment;
        this.missingVariableLevel = missingVariableLevel;
        this.missingVariableComment = missingVariableComment;
        this.counterargumentLevel = counterargumentLevel;
        this.counterargumentComment = counterargumentComment;
        this.duplicationScore = duplicationScore;
        this.mostSimilarCardId = mostSimilarCardId;
        this.mostSimilarCardTitle = mostSimilarCardTitle;
        this.overallComment = overallComment;
        this.reviewedAt = reviewedAt;
    }
}
