package whispy_server.whispy.domain.statistics.activity.model;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * 일일 활동 데이터.
 *
 * 특정 날짜의 활동 통계를 나타냅니다.
 *
 * @param date 날짜
 * @param dayOfWeek 요일
 * @param totalMinutes 누적 활동 시간(분)
 */
public record DayActivityData(
        LocalDate date,
        DayOfWeek dayOfWeek,
        int totalMinutes
) {
}
