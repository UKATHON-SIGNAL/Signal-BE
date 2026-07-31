package com.signal.signalbe.client.signalai.dto;

import com.signal.signalbe.domain.briefing.TrendDirection;

public record BriefingInsightDto(String title, TrendDirection trend) {
}
