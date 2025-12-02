package whispy_server.whispy.domain.statistics.activity.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.statistics.activity.model.MonthIndicator;
import whispy_server.whispy.domain.statistics.activity.model.WeekActivityData;

import java.time.LocalDate;
import java.util.List;

/**
 * 주간 활동 통계 응답 DTO.
 *
 * 사용자의 주간 활동 통계 정보를 포함합니다.
 *
 * @param startDate 조회 시작 날짜
 * @param endDate 조회 종료 날짜
 * @param months 월 표시 목록
 * @param weeks 주별 활동 데이터 목록
 */
@Schema(description = "주간 활동 응답")
public record WeeklyActivityResponse(
        @Schema(description = "시작 날짜", example = "2024-01-01")
        LocalDate startDate,
        @Schema(description = "종료 날짜", example = "2024-12-31")
        LocalDate endDate,
        @Schema(description = "월 표시 목록")
        List<MonthIndicator> months,
        @Schema(description = "주별 활동 데이터 목록")
        List<WeekActivityData> weeks
) {
}
