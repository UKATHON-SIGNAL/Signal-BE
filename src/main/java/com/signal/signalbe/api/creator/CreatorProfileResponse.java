package com.signal.signalbe.api.creator;

import com.signal.signalbe.domain.user.CreatorGrade;
import com.signal.signalbe.domain.user.CreatorProfile;

import java.math.BigDecimal;

public record CreatorProfileResponse(
        Long userId,
        int totalPublishedCount,
        int totalEvaluatedCount,
        int successCount,
        BigDecimal successRate,
        BigDecimal reputationScore,
        CreatorGrade grade
) {
    public static CreatorProfileResponse from(CreatorProfile profile) {
        return new CreatorProfileResponse(
                profile.getUser().getId(),
                profile.getTotalPublishedCount(),
                profile.getTotalEvaluatedCount(),
                profile.getSuccessCount(),
                profile.getSuccessRate(),
                profile.getReputationScore(),
                profile.getGrade()
        );
    }
}
