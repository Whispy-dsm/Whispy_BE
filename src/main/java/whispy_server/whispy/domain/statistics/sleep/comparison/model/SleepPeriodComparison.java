package whispy_server.whispy.domain.statistics.sleep.comparison.model;

import whispy_server.whispy.global.annotation.Aggregate;

/**
 * 수면 기간 비교 애그리게잇.
 *
 * 수면 통계의 기간별 비교 데이터를 나타냅니다.
 *
 * @param currentPeriodMinutes 현재 기간 수면 시간(분)
 * @param previousPeriodMinutes 이전 기간 수면 시간(분)
 * @param twoPeriodAgoMinutes 2기간 전 수면 시간(분)
 * @param differenceFromPrevious 이전 기간 대비 차이(분)
 */
@Aggregate
public record SleepPeriodComparison(
        int currentPeriodMinutes,
        int previousPeriodMinutes,
        int twoPeriodAgoMinutes,
        int differenceFromPrevious
) {
}
