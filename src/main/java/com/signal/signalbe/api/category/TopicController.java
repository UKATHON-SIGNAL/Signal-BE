package com.signal.signalbe.api.category;

import com.signal.signalbe.domain.category.Topic;
import com.signal.signalbe.domain.category.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicRepository topicRepository;

    @GetMapping
    public List<TopicResponse> getTopics(@RequestParam(required = false) Long categoryId) {
        List<Topic> topics = categoryId != null
                ? topicRepository.findByCategoryId(categoryId)
                : topicRepository.findAll();
        return topics.stream()
                .filter(Topic::isActive)
                .map(TopicResponse::from)
                .toList();
    }
}
