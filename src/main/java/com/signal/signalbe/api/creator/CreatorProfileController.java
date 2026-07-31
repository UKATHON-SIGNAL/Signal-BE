package com.signal.signalbe.api.creator;

import com.signal.signalbe.domain.user.CreatorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/{userId}/creator-profile")
@RequiredArgsConstructor
public class CreatorProfileController {

    private final CreatorProfileService creatorProfileService;

    @GetMapping
    public CreatorProfileResponse getCreatorProfile(@PathVariable Long userId) {
        var profile = creatorProfileService.getByUserId(userId);
        var sourceReliability = creatorProfileService.getSourceReliability(userId);
        return CreatorProfileResponse.from(profile, sourceReliability);
    }
}
