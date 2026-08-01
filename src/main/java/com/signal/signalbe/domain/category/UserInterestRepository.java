package com.signal.signalbe.domain.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {

    List<UserInterest> findByUserId(Long userId);

    boolean existsByUserIdAndCategoryId(Long userId, Long categoryId);

    void deleteByUserIdAndCategoryId(Long userId, Long categoryId);
}
