package com.signal.signalbe.api.interest;

import com.signal.signalbe.domain.category.UserInterestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/interests")
@RequiredArgsConstructor
public class UserInterestController {

    private final UserInterestService userInterestService;

    @GetMapping
    public List<UserInterestResponse> getInterests(@PathVariable Long userId) {
        return userInterestService.getInterests(userId).stream().map(UserInterestResponse::from).toList();
    }

    @PostMapping
    public ResponseEntity<UserInterestResponse> addInterest(
            @PathVariable Long userId, @Valid @RequestBody UserInterestCreateRequest request) {
        var interest = userInterestService.addInterest(userId, request.topicId());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserInterestResponse.from(interest));
    }

    @DeleteMapping("/{topicId}")
    public ResponseEntity<Void> removeInterest(@PathVariable Long userId, @PathVariable Long topicId) {
        userInterestService.removeInterest(userId, topicId);
        return ResponseEntity.noContent().build();
    }
}
