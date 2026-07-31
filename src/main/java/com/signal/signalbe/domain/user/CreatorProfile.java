package com.signal.signalbe.domain.user;

import com.signal.signalbe.domain.common.BaseTimeEntity;
import com.signal.signalbe.domain.result.ResultVerdict;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "creator_profiles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatorProfile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CreatorVerificationStatus verificationStatus;

    @Column(length = 200)
    private String bio;

    private LocalDateTime verifiedAt;

    @Column(nullable = false)
    private int totalPublishedCount;

    @Column(nullable = false)
    private int totalEvaluatedCount;

    @Column(nullable = false)
    private int invalidCount;

    @Column(nullable = false)
    private BigDecimal totalScore;

    @Column(nullable = false)
    private BigDecimal averageScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CreatorGrade grade;

    @Enumerated(EnumType.STRING)
    private CreatorTrend trend;

    public CreatorProfile(User user) {
        this.user = user;
        this.verificationStatus = CreatorVerificationStatus.UNVERIFIED;
        this.totalPublishedCount = 0;
        this.totalEvaluatedCount = 0;
        this.invalidCount = 0;
        this.totalScore = BigDecimal.ZERO;
        this.averageScore = BigDecimal.ZERO;
        this.grade = CreatorGrade.UNRATED;
    }

    public void increasePublishedCount() {
        this.totalPublishedCount += 1;
    }

    public void applyEvaluatedResult(ResultVerdict verdict) {
        if (verdict == ResultVerdict.INVALID) {
            this.invalidCount += 1;
            return;
        }

        BigDecimal previousAverage = this.averageScore;

        this.totalEvaluatedCount += 1;
        this.totalScore = this.totalScore.add(BigDecimal.valueOf(verdict.getScore()));
        this.averageScore = this.totalScore
                .divide(BigDecimal.valueOf(this.totalEvaluatedCount), 2, RoundingMode.HALF_UP);
        this.grade = computeGrade(this.averageScore);
        this.trend = computeTrend(previousAverage, this.averageScore);
    }

    private static CreatorGrade computeGrade(BigDecimal averageScore) {
        if (averageScore.compareTo(BigDecimal.valueOf(80)) >= 0) {
            return CreatorGrade.A;
        }
        if (averageScore.compareTo(BigDecimal.valueOf(50)) >= 0) {
            return CreatorGrade.B;
        }
        return CreatorGrade.C;
    }

    private static CreatorTrend computeTrend(BigDecimal before, BigDecimal after) {
        int comparison = after.compareTo(before);
        if (comparison > 0) {
            return CreatorTrend.UP;
        }
        if (comparison < 0) {
            return CreatorTrend.DOWN;
        }
        return CreatorTrend.FLAT;
    }
}
