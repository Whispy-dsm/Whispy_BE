package whispy_server.whispy.domain.statistics.sleep.summary.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.statistics.sleep.summary.model.SleepStatistics;
import java.time.LocalTime;

/**
 * 수면 통계 응답 DTO.
 *
 * 수면 통계의 요약 데이터를 포함합니다.
 *
 * @param todayMinutes 오늘 수면 시간(분)
 * @param averageMinutes 평균 수면 시간(분)
 * @param sleepConsistency 수면 일관성(0.0-1.0)
 * @param averageBedTime 평균 취침 시각
 * @param averageWakeTime 평균 기상 시각
 * @param totalMinutes 총 수면 시간(분)
 * @param totalCount 총 수면 세션 수
 */
@Schema(description = "수면 통계 응답")
public record SleepStatisticsResponse(
        @Schema(description = "오늘 수면 시간(분)", example = "480")
        int todayMinutes,
        @Schema(description = "평균 수면 시간(분)", example = "450")
        int averageMinutes,
        @Schema(description = "수면 일관성", example = "0.85")
        double sleepConsistency,
        @Schema(description = "평균 취침 시각", example = "23:00:00")
        LocalTime averageBedTime,
        @Schema(description = "평균 기상 시각", example = "07:00:00")
        LocalTime averageWakeTime,
        @Schema(description = "총 수면 시간(분)", example = "13500")
        int totalMinutes,
        @Schema(description = "총 수면 세션 수", example = "30")
        int totalCount
) {
    public static SleepStatisticsResponse from(SleepStatistics statistics) {
        return new SleepStatisticsResponse(
                statistics.todayMinutes(),
                statistics.averageMinutes(),
                statistics.sleepConsistency(),
                statistics.averageBedTime(),
                statistics.averageWakeTime(),
                statistics.totalMinutes(),
                statistics.totalCount()
        );
    }
}
