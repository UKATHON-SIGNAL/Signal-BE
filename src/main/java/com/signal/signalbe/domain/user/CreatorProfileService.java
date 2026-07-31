package com.signal.signalbe.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatorProfileService {

    private final CreatorProfileRepository creatorProfileRepository;

    public CreatorProfile getByUserId(Long userId) {
        return creatorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("작성자 성과 정보가 없습니다. userId=" + userId));
    }
}
