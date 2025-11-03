package whispy_server.whispy.domain.statistics.focus.comparison.adapter.in.web.dto.response;

import whispy_server.whispy.domain.statistics.focus.comparison.model.PeriodComparisonStatistics;

public record PeriodComparisonResponse(
        int currentPeriodMinutes,
        int previousPeriodMinutes,
        int twoPeriodAgoMinutes,
        int differenceFromPrevious
) {
    public static PeriodComparisonResponse from(PeriodComparisonStatistics statistics) {
        return new PeriodComparisonResponse(
                statistics.currentPeriodMinutes(),
                statistics.previousPeriodMinutes(),
                statistics.twoPeriodAgoMinutes(),
                statistics.differenceFromPrevious()
        );
    }
}
