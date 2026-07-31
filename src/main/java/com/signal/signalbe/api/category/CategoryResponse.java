package com.signal.signalbe.api.category;

import com.signal.signalbe.domain.category.Category;

public record CategoryResponse(Long id, String categoryName, String slug) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getSlug());
    }
}
