package com.signal.signalbe.api.home;

import com.signal.signalbe.domain.home.HomeSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/{userId}/home-summary")
@RequiredArgsConstructor
public class HomeSummaryController {

    private final HomeSummaryService homeSummaryService;

    @GetMapping
    public HomeSummaryResponse getSummary(@PathVariable Long userId) {
        return HomeSummaryResponse.from(homeSummaryService.getSummary(userId));
    }
}
