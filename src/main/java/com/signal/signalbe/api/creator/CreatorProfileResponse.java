package com.signal.signalbe.api.creator;

import com.signal.signalbe.domain.user.CreatorGrade;
import com.signal.signalbe.domain.user.CreatorProfile;
import com.signal.signalbe.domain.user.CreatorTrend;
import com.signal.signalbe.domain.user.CreatorVerificationStatus;

import java.math.BigDecimal;

public record CreatorProfileResponse(
        Long userId,
        String bio,
        CreatorVerificationStatus verificationStatus,
        int totalPublishedCount,
        int totalEvaluatedCount,
        int invalidCount,
        BigDecimal averageScore,
        BigDecimal sourceReliability,
        CreatorGrade grade,
        CreatorTrend trend
) {
    public static CreatorProfileResponse from(CreatorProfile profile, BigDecimal sourceReliability) {
        return new CreatorProfileResponse(
                profile.getUser().getId(),
                profile.getBio(),
                profile.getVerificationStatus(),
                profile.getTotalPublishedCount(),
                profile.getTotalEvaluatedCount(),
                profile.getInvalidCount(),
                profile.getAverageScore(),
                sourceReliability,
                profile.getGrade(),
                profile.getTrend()
        );
    }
}
