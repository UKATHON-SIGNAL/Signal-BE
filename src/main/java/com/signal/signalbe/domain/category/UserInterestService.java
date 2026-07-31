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
    private final TopicRepository topicRepository;

    public List<UserInterest> getInterests(Long userId) {
        return userInterestRepository.findByUserId(userId);
    }

    @Transactional
    public UserInterest addInterest(Long userId, Long topicId) {
        if (userInterestRepository.existsByUserIdAndTopicId(userId, topicId)) {
            throw new IllegalStateException("이미 추가된 관심 주제입니다.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. id=" + userId));
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 토픽입니다. id=" + topicId));
        return userInterestRepository.save(new UserInterest(user, topic));
    }

    @Transactional
    public void removeInterest(Long userId, Long topicId) {
        userInterestRepository.deleteByUserIdAndTopicId(userId, topicId);
    }
}
