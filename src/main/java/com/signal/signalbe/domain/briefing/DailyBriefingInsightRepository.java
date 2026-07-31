package com.signal.signalbe.domain.briefing;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DailyBriefingInsightRepository extends JpaRepository<DailyBriefingInsight, Long> {

    List<DailyBriefingInsight> findByBriefingDateOrderBySortOrder(LocalDate briefingDate);
}
