package whispy_server.whispy.domain.statistics.focus.summary.model;

import whispy_server.whispy.domain.focussession.model.types.FocusTag;
import whispy_server.whispy.global.annotation.Aggregate;

import java.util.Map;

/**
 * 집중 통계 애그리게잇.
 *
 * 집중 통계의 요약 데이터를 나타냅니다.
 *
 * @param totalCount 총 세션 수
 * @param totalMinutes 누적 집중 시간(분)
 * @param todayMinutes 오늘 집중 시간(분)
 * @param tagMinutes 태그별 집중 시간 맵
 */
@Aggregate
public record FocusStatistics(
        int totalCount,
        int totalMinutes,
        int todayMinutes,
        Map<FocusTag, Integer> tagMinutes
) {
}
