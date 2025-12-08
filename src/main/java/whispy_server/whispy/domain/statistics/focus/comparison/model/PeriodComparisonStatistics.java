package whispy_server.whispy.domain.statistics.focus.comparison.model;

import whispy_server.whispy.global.annotation.Aggregate;

/**
 * 집중 기간 비교 통계 애그리게잇.
 *
 * 집중 통계의 기간별 비교 데이터를 나타냅니다.
 *
 * @param currentPeriodMinutes 현재 기간 집중 시간(분)
 * @param previousPeriodMinutes 이전 기간 집중 시간(분)
 * @param twoPeriodAgoMinutes 2기간 전 집중 시간(분)
 * @param differenceFromPrevious 이전 기간 대비 차이(분)
 */
@Aggregate
public record PeriodComparisonStatistics(
        int currentPeriodMinutes,
        int previousPeriodMinutes,
        int twoPeriodAgoMinutes,
        int differenceFromPrevious
) {
}
