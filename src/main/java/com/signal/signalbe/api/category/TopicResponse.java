package com.signal.signalbe.api.category;

import com.signal.signalbe.domain.category.Topic;

public record TopicResponse(Long id, Long categoryId, String topicName, String slug) {
    public static TopicResponse from(Topic topic) {
        return new TopicResponse(topic.getId(), topic.getCategory().getId(), topic.getName(), topic.getSlug());
    }
}
