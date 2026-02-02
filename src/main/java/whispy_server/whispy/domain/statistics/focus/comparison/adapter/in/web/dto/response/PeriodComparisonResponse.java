package whispy_server.whispy.domain.statistics.focus.comparison.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.statistics.focus.comparison.model.PeriodComparisonStatistics;

/**
 * 집중 기간 비교 응답 DTO.
 *
 * 집중 통계의 기간별 비교 데이터를 포함합니다.
 *
 * @param currentPeriodMinutes 현재 기간 집중 시간(분)
 * @param previousPeriodMinutes 이전 기간 집중 시간(분)
 * @param twoPeriodAgoMinutes 2기간 전 집중 시간(분)
 * @param differenceFromPrevious 이전 기간 대비 차이(분)
 */
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
    /**
     * PeriodComparisonStatistics 도메인 모델을 PeriodComparisonResponse로 변환합니다.
     *
     * @param statistics 집중 기간 비교 통계 도메인 모델
     * @return 집중 기간 비교 응답 DTO
     */
    public static PeriodComparisonResponse from(PeriodComparisonStatistics statistics) {
        return new PeriodComparisonResponse(
                statistics.currentPeriodMinutes(),
                statistics.previousPeriodMinutes(),
                statistics.twoPeriodAgoMinutes(),
                statistics.differenceFromPrevious()
        );
    }
}
