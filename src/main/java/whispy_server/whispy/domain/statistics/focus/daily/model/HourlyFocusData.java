package whispy_server.whispy.domain.statistics.focus.daily.model;

import java.util.List;

/**
 * 시간별 집중 데이터.
 *
 * 특정 시간의 집중 통계를 나타냅니다.
 *
 * @param hour 시간(0-23)
 * @param minutes 집중 시간(분)
 * @param tagData 태그별 집중 데이터 목록
 */
public record HourlyFocusData(
        int hour,
        int minutes,
        List<TagFocusData> tagData
) {
}
