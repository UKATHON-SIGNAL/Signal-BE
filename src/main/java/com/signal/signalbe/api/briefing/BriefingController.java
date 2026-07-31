package com.signal.signalbe.api.briefing;

import com.signal.signalbe.domain.briefing.DailyBriefingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/briefing")
@RequiredArgsConstructor
public class BriefingController {

    private final DailyBriefingService dailyBriefingService;

    @GetMapping("/today")
    public List<BriefingInsightResponse> getTodayBriefing() {
        return dailyBriefingService.getTodayInsights().stream().map(BriefingInsightResponse::from).toList();
    }
}
