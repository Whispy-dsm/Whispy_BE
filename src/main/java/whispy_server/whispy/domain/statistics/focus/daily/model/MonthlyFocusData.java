package whispy_server.whispy.domain.statistics.focus.daily.model;

import java.time.Month;
import java.util.List;

/**
 * 월별 집중 데이터.
 *
 * 특정 월의 집중 통계를 나타냅니다.
 *
 * @param month 월(1-12)
 * @param monthName 월 이름
 * @param minutes 월간 집중 시간(분)
 * @param tagData 태그별 집중 데이터 목록
 */
public record MonthlyFocusData(
        int month,
        Month monthName,
        int minutes,
        List<TagFocusData> tagData
) {
}
