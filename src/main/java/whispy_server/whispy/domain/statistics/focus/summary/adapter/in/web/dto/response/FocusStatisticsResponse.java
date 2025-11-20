package whispy_server.whispy.domain.statistics.focus.summary.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;
import whispy_server.whispy.domain.statistics.focus.summary.model.FocusStatistics;

import java.util.Map;

@Schema(description = "집중 통계 응답")
public record FocusStatisticsResponse(
        @Schema(description = "총 집중 세션 수", example = "50")
        int totalCount,
        @Schema(description = "총 집중 시간(분)", example = "1500")
        int totalMinutes,
        @Schema(description = "오늘 집중 시간(분)", example = "120")
        int todayMinutes,
        @Schema(description = "태그별 집중 시간(분)")
        Map<FocusTag, Integer> tagMinutes
) {
    public static FocusStatisticsResponse from(FocusStatistics statistics) {
        return new FocusStatisticsResponse(
                statistics.totalCount(),
                statistics.totalMinutes(),
                statistics.todayMinutes(),
                statistics.tagMinutes()
        );
    }
}
