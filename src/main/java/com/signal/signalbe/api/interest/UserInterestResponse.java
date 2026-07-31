package com.signal.signalbe.api.interest;

import com.signal.signalbe.domain.category.UserInterest;

public record UserInterestResponse(Long topicId, String topicName, Long categoryId, String categoryName) {
    public static UserInterestResponse from(UserInterest interest) {
        var topic = interest.getTopic();
        var category = topic.getCategory();
        return new UserInterestResponse(
                topic.getId(),
                topic.getName(),
                category != null ? category.getId() : null,
                category != null ? category.getName() : null
        );
    }
}
