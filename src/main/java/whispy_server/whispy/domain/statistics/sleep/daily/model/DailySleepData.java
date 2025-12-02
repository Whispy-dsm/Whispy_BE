package whispy_server.whispy.domain.statistics.sleep.daily.model;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * 일일 수면 데이터.
 *
 * 특정 날짜의 수면 통계를 나타냅니다.
 *
 * @param date 날짜
 * @param dayOfWeek 요일
 * @param day 날짜 숫자(1-31)
 * @param minutes 수면 시간(분)
 */
public record DailySleepData(
        LocalDate date,
        DayOfWeek dayOfWeek,
        int day,
        int minutes
) {
}
