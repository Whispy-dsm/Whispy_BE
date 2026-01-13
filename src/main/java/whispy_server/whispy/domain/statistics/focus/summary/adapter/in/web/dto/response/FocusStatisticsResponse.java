package whispy_server.whispy.domain.statistics.focus.summary.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;
import whispy_server.whispy.domain.statistics.focus.summary.model.FocusStatistics;

import java.util.Map;

/**
 * 집중 통계 응답 DTO.
 *
 * 집중 통계의 요약 데이터를 포함합니다.
 *
 * @param totalCount 총 집중 세션 수
 * @param totalMinutes 누적 집중 시간(분)
 * @param todayMinutes 오늘 집중 시간(분)
 * @param totalDays 누적 집중 일수
 * @param tagMinutes 태그별 집중 시간 맵
 */
@Schema(description = "집중 통계 응답")
public record FocusStatisticsResponse(
        @Schema(description = "총 집중 세션 수", example = "50")
        int totalCount,
        @Schema(description = "총 집중 시간(분)", example = "1500")
        int totalMinutes,
        @Schema(description = "오늘 집중 시간(분)", example = "120")
        int todayMinutes,
        @Schema(description = "누적 집중 일수", example = "30")
        int totalDays,
        @Schema(description = "태그별 집중 시간(분)")
        Map<FocusTag, Integer> tagMinutes
) {
    public static FocusStatisticsResponse from(FocusStatistics statistics) {
        return new FocusStatisticsResponse(
                statistics.totalCount(),
                statistics.totalMinutes(),
                statistics.todayMinutes(),
                statistics.totalDays(),
                statistics.tagMinutes()
        );
    }
}
