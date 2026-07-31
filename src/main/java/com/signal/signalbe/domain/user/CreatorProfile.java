package com.signal.signalbe.domain.user;

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
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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

    @Lob
    private String bio;

    private LocalDateTime verifiedAt;

    @Column(nullable = false)
    private int totalPublishedCount;

    @Column(nullable = false)
    private int totalEvaluatedCount;

    @Column(nullable = false)
    private int successCount;

    @Column(nullable = false)
    private BigDecimal successRate;

    @Column(nullable = false)
    private BigDecimal reputationScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CreatorGrade grade;

    private BigDecimal percentileRank;

    public CreatorProfile(User user) {
        this.user = user;
        this.verificationStatus = CreatorVerificationStatus.UNVERIFIED;
        this.totalPublishedCount = 0;
        this.totalEvaluatedCount = 0;
        this.successCount = 0;
        this.successRate = BigDecimal.ZERO;
        this.reputationScore = BigDecimal.ZERO;
        this.grade = CreatorGrade.UNRATED;
    }

    public void increasePublishedCount() {
        this.totalPublishedCount += 1;
    }

    public void applyEvaluatedResult(boolean isSuccess) {
        this.totalEvaluatedCount += 1;
        if (isSuccess) {
            this.successCount += 1;
        }
        this.successRate = BigDecimal.valueOf(this.successCount)
                .divide(BigDecimal.valueOf(this.totalEvaluatedCount), 2, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        this.reputationScore = this.successRate;
        this.grade = computeGrade(this.successRate);
    }

    private static CreatorGrade computeGrade(BigDecimal successRate) {
        if (successRate.compareTo(BigDecimal.valueOf(80)) >= 0) {
            return CreatorGrade.A;
        }
        if (successRate.compareTo(BigDecimal.valueOf(50)) >= 0) {
            return CreatorGrade.B;
        }
        return CreatorGrade.C;
    }
}
