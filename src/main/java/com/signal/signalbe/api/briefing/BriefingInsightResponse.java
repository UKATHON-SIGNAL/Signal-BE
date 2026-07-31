package com.signal.signalbe.api.briefing;

import com.signal.signalbe.domain.briefing.DailyBriefingInsight;
import com.signal.signalbe.domain.briefing.TrendDirection;

public record BriefingInsightResponse(String title, TrendDirection trend) {
    public static BriefingInsightResponse from(DailyBriefingInsight insight) {
        return new BriefingInsightResponse(insight.getTitle(), insight.getTrendDirection());
    }
}
