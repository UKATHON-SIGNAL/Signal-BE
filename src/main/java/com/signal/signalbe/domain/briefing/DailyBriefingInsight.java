package com.signal.signalbe.domain.briefing;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(
        name = "daily_briefing_insights",
        uniqueConstraints = @UniqueConstraint(columnNames = {"briefing_date", "sort_order"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyBriefingInsight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "briefing_date", nullable = false)
    private LocalDate briefingDate;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrendDirection trendDirection;

    @Column(nullable = false)
    private int sortOrder;

    public DailyBriefingInsight(LocalDate briefingDate, String title, TrendDirection trendDirection, int sortOrder) {
        this.briefingDate = briefingDate;
        this.title = title;
        this.trendDirection = trendDirection;
        this.sortOrder = sortOrder;
    }
}
