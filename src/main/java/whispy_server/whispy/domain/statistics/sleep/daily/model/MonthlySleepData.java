package whispy_server.whispy.domain.statistics.sleep.daily.model;

import java.time.Month;

/**
 * 월별 수면 데이터.
 *
 * 특정 월의 수면 통계를 나타냅니다.
 *
 * @param month 월(1-12)
 * @param monthName 월 이름
 * @param minutes 월간 수면 시간(분)
 */
public record MonthlySleepData(
        int month,
        Month monthName,
        int minutes
) {
}
