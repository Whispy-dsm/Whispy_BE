package whispy_server.whispy.domain.statistics.focus.daily.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * 일일 집중 데이터.
 *
 * 특정 날짜의 집중 통계를 나타냅니다.
 *
 * @param date 날짜
 * @param dayOfWeek 요일
 * @param day 날짜 숫자(1-31)
 * @param minutes 집중 시간(분)
 * @param tagData 태그별 집중 데이터 목록
 */
public record DailyFocusData(
        LocalDate date,
        DayOfWeek dayOfWeek,
        int day,
        int minutes,
        List<TagFocusData> tagData
) {
}
