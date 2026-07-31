package com.signal.signalbe.api.user;

import com.signal.signalbe.domain.user.User;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String email,
        String nickname,
        String profileImageUrl,
        LocalDateTime joinedAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(), user.getEmail(), user.getNickname(), user.getProfileImageUrl(), user.getCreatedAt());
    }
}
