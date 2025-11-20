package whispy_server.whispy.domain.statistics.focus.comparison.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.statistics.focus.comparison.model.PeriodComparisonStatistics;

@Schema(description = "집중 기간 비교 응답")
public record PeriodComparisonResponse(
        @Schema(description = "현재 기간 집중 시간(분)", example = "120")
        int currentPeriodMinutes,
        @Schema(description = "이전 기간 집중 시간(분)", example = "100")
        int previousPeriodMinutes,
        @Schema(description = "2기간 전 집중 시간(분)", example = "80")
        int twoPeriodAgoMinutes,
        @Schema(description = "이전 기간 대비 차이(분)", example = "20")
        int differenceFromPrevious
) {
    public static PeriodComparisonResponse from(PeriodComparisonStatistics statistics) {
        return new PeriodComparisonResponse(
                statistics.currentPeriodMinutes(),
                statistics.previousPeriodMinutes(),
                statistics.twoPeriodAgoMinutes(),
                statistics.differenceFromPrevious()
        );
    }
}
