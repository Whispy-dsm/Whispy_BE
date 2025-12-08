package whispy_server.whispy.domain.statistics.activity.model;

import java.time.Month;

/**
 * 월 표시 데이터.
 *
 * 주간 활동 표시에서 월 변화를 표시합니다.
 *
 * @param year 연도
 * @param month 월(1-12)
 * @param monthName 월 이름
 * @param startWeekIndex 해당 월이 시작하는 주의 인덱스
 */
public record MonthIndicator(
        int year,
        int month,
        Month monthName,
        int startWeekIndex
) {
}
