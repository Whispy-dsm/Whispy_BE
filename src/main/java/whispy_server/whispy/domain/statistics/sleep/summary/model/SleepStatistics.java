package whispy_server.whispy.domain.statistics.sleep.summary.model;

import whispy_server.whispy.global.annotation.Aggregate;
import java.time.LocalTime;

/**
 * 수면 통계 애그리게잇.
 *
 * 수면 통계의 요약 데이터를 나타냅니다.
 *
 * @param todayMinutes 오늘 수면 시간(분)
 * @param averageMinutes 평균 수면 시간(분)
 * @param sleepConsistency 수면 일관성(0.0-1.0)
 * @param averageBedTime 평균 취침 시간
 * @param averageWakeTime 평균 기상 시간
 * @param totalMinutes 누적 수면 시간(분)
 * @param totalCount 총 수면 세션 수
 */
@Aggregate
public record SleepStatistics(
        int todayMinutes,
        int averageMinutes,
        double sleepConsistency,
        LocalTime averageBedTime,
        LocalTime averageWakeTime,
        int totalMinutes,
        int totalCount
) {
}
