package com.signal.signalbe.api.interest;

import com.signal.signalbe.domain.category.UserInterest;

public record UserInterestResponse(Long categoryId, String categoryName) {
    public static UserInterestResponse from(UserInterest interest) {
        var category = interest.getCategory();
        return new UserInterestResponse(category.getId(), category.getName());
    }
}
