package whispy_server.whispy.domain.statistics.sleep.comparison.adapter.in.web.dto.response;

import whispy_server.whispy.domain.statistics.sleep.comparison.model.SleepPeriodComparison;

public record SleepPeriodComparisonResponse(
        int currentPeriodMinutes,
        int previousPeriodMinutes,
        int twoPeriodAgoMinutes,
        int differenceFromPrevious
) {
    public static SleepPeriodComparisonResponse from(SleepPeriodComparison comparison) {
        return new SleepPeriodComparisonResponse(
                comparison.currentPeriodMinutes(),
                comparison.previousPeriodMinutes(),
                comparison.twoPeriodAgoMinutes(),
                comparison.differenceFromPrevious()
        );
    }
}
