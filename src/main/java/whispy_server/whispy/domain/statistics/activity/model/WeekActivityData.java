package whispy_server.whispy.domain.statistics.activity.model;

import java.util.List;

/**
 * 주간 활동 데이터.
 *
 * 특정 주의 활동 통계를 나타냅니다.
 *
 * @param weekIndex 주의 인덱스(0부터 시작)
 * @param days 해당 주의 일일 활동 데이터 목록
 */
public record WeekActivityData(
        int weekIndex,
        List<DayActivityData> days
) {
}
