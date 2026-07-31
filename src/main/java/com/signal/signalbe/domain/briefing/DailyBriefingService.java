package com.signal.signalbe.domain.briefing;

import com.signal.signalbe.client.signalai.SignalAiClient;
import com.signal.signalbe.client.signalai.dto.BriefingRequest;
import com.signal.signalbe.client.signalai.dto.BriefingResponse;
import com.signal.signalbe.client.signalai.dto.CardSummaryInput;
import com.signal.signalbe.domain.card.Card;
import com.signal.signalbe.domain.card.CardRepository;
import com.signal.signalbe.domain.card.CardStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DailyBriefingService {

    private static final int MAX_SOURCE_CARDS = 15;

    private final DailyBriefingInsightRepository dailyBriefingInsightRepository;
    private final CardRepository cardRepository;
    private final SignalAiClient signalAiClient;

    @Transactional
    public List<DailyBriefingInsight> getTodayInsights() {
        LocalDate today = LocalDate.now();
        List<DailyBriefingInsight> existing = dailyBriefingInsightRepository.findByBriefingDateOrderBySortOrder(today);
        if (!existing.isEmpty()) {
            return existing;
        }
        return generateTodayInsights(today);
    }

    private List<DailyBriefingInsight> generateTodayInsights(LocalDate today) {
        List<CardSummaryInput> cardInputs = cardRepository.findByStatus(CardStatus.PUBLISHED).stream()
                .sorted(Comparator.comparing(Card::getPublishedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(MAX_SOURCE_CARDS)
                .map(card -> new CardSummaryInput(card.getClaim(), card.getCategory().getName()))
                .toList();

        BriefingResponse response = signalAiClient.briefing(new BriefingRequest(cardInputs));

        List<DailyBriefingInsight> insights = new ArrayList<>();
        int sortOrder = 1;
        for (var insight : response.insights()) {
            insights.add(dailyBriefingInsightRepository.save(
                    new DailyBriefingInsight(today, insight.title(), insight.trend(), sortOrder)));
            sortOrder++;
        }
        return insights;
    }
}
