package com.signal.signalbe.domain.user;

import com.signal.signalbe.domain.verification.AiVerification;
import com.signal.signalbe.domain.verification.AiVerificationRepository;
import com.signal.signalbe.domain.verification.ReviewLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatorProfileService {

    private final CreatorProfileRepository creatorProfileRepository;
    private final AiVerificationRepository aiVerificationRepository;

    public CreatorProfile getByUserId(Long userId) {
        return creatorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("작성자 성과 정보가 없습니다. userId=" + userId));
    }

    public BigDecimal getSourceReliability(Long userId) {
        List<AiVerification> latestPerCard = aiVerificationRepository.findByCard_Author_Id(userId).stream()
                .collect(Collectors.groupingBy(
                        av -> av.getCard().getId(),
                        Collectors.maxBy(Comparator.comparing(AiVerification::getCreatedAt))))
                .values().stream()
                .flatMap(Optional::stream)
                .filter(av -> av.getEvidenceRelevanceLevel() != null)
                .toList();

        if (latestPerCard.isEmpty()) {
            return BigDecimal.ZERO;
        }

        int totalScore = latestPerCard.stream()
                .mapToInt(av -> reviewLevelScore(av.getEvidenceRelevanceLevel()))
                .sum();

        return BigDecimal.valueOf(totalScore)
                .divide(BigDecimal.valueOf(latestPerCard.size()), 0, RoundingMode.HALF_UP);
    }

    private static int reviewLevelScore(ReviewLevel level) {
        return switch (level) {
            case HIGH -> 100;
            case MEDIUM -> 66;
            case LOW -> 33;
            case CAUTION -> 0;
        };
    }
}
