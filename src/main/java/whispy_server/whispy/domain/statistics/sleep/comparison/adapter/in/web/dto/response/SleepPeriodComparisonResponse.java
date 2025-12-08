package whispy_server.whispy.domain.statistics.sleep.comparison.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.statistics.sleep.comparison.model.SleepPeriodComparison;

/**
 * 수면 기간 비교 응답 DTO.
 *
 * 수면 통계의 기간별 비교 데이터를 포함합니다.
 *
 * @param currentPeriodMinutes 현재 기간 수면 시간(분)
 * @param previousPeriodMinutes 이전 기간 수면 시간(분)
 * @param twoPeriodAgoMinutes 2기간 전 수면 시간(분)
 * @param differenceFromPrevious 이전 기간 대비 차이(분)
 */
@Schema(description = "수면 기간 비교 응답")
public record SleepPeriodComparisonResponse(
        @Schema(description = "현재 기간 수면 시간(분)", example = "480")
        int currentPeriodMinutes,
        @Schema(description = "이전 기간 수면 시간(분)", example = "420")
        int previousPeriodMinutes,
        @Schema(description = "2기간 전 수면 시간(분)", example = "400")
        int twoPeriodAgoMinutes,
        @Schema(description = "이전 기간 대비 차이(분)", example = "60")
        int differenceFromPrevious
) {
    public static SleepPeriodComparisonResponse from(SleepPeriodComparison comparison) {
        return new SleepPeriodComparisonResponse(
                comparison.currentPeriodMinutes(),
                comparison.previousPeriodMinutes(),
                comparison.twoPeriodAgoMinutes(),
                comparison.differenceFromPrevious()
        );
    }
}
