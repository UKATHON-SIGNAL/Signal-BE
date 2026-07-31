package com.signal.signalbe.domain.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {

    List<UserInterest> findByUserId(Long userId);

    boolean existsByUserIdAndTopicId(Long userId, Long topicId);

    void deleteByUserIdAndTopicId(Long userId, Long topicId);
}
