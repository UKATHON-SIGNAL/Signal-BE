package com.signal.signalbe.domain.category;

import com.signal.signalbe.domain.user.User;
import com.signal.signalbe.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserInterestService {

    private final UserInterestRepository userInterestRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public List<UserInterest> getInterests(Long userId) {
        return userInterestRepository.findByUserId(userId);
    }

    @Transactional
    public UserInterest addInterest(Long userId, Long categoryId) {
        if (userInterestRepository.existsByUserIdAndCategoryId(userId, categoryId)) {
            throw new IllegalStateException("이미 추가된 관심 주제입니다.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. id=" + userId));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다. id=" + categoryId));
        return userInterestRepository.save(new UserInterest(user, category));
    }

    @Transactional
    public void removeInterest(Long userId, Long categoryId) {
        userInterestRepository.deleteByUserIdAndCategoryId(userId, categoryId);
    }
}
