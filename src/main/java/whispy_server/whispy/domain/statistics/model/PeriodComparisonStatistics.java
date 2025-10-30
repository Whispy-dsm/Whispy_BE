package whispy_server.whispy.domain.statistics.model;

import whispy_server.whispy.global.annotation.Aggregate;

@Aggregate
public record PeriodComparisonStatistics(
        int currentPeriodMinutes,
        int previousPeriodMinutes,
        int twoPeriodAgoMinutes,
        int differenceFromPrevious
) {
}