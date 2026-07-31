package com.signal.signalbe.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreatorProfileRepository extends JpaRepository<CreatorProfile, Long> {

    Optional<CreatorProfile> findByUserId(Long userId);
}
