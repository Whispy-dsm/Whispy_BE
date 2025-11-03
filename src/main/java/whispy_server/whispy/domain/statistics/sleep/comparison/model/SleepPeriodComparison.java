package whispy_server.whispy.domain.statistics.sleep.comparison.model;

import whispy_server.whispy.global.annotation.Aggregate;

@Aggregate
public record SleepPeriodComparison(
        int currentPeriodMinutes,
        int previousPeriodMinutes,
        int twoPeriodAgoMinutes,
        int differenceFromPrevious
) {
}
