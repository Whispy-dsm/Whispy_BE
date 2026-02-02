package whispy_server.whispy.domain.statistics.focus.daily.adapter.in.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.statistics.focus.daily.model.DailyFocusData;
import whispy_server.whispy.domain.statistics.focus.daily.model.DailyFocusStatistics;
import whispy_server.whispy.domain.statistics.focus.daily.model.HourlyFocusData;
import whispy_server.whispy.domain.statistics.focus.daily.model.MonthlyFocusData;

import java.util.List;

/**
 * 일일 집중 통계 응답 DTO.
 *
 * 집중 통계의 시간별, 일별, 월별 상세 데이터를 포함합니다.
 *
 * @param hourlyData 시간별 집중 데이터 목록
 * @param dailyData 일별 집중 데이터 목록
 * @param monthlyData 월별 집중 데이터 목록
 */
@Schema(description = "일별 집중 통계 응답")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DailyFocusStatisticsResponse(
        @Schema(description = "시간별 집중 데이터")
        List<HourlyFocusData> hourlyData,
        @Schema(description = "일별 집중 데이터")
        List<DailyFocusData> dailyData,
        @Schema(description = "월별 집중 데이터")
        List<MonthlyFocusData> monthlyData
) {
    /**
     * DailyFocusStatistics 도메인 모델을 DailyFocusStatisticsResponse로 변환합니다.
     *
     * @param statistics 일별 집중 통계 도메인 모델
     * @return 일별 집중 통계 응답 DTO
     */
    public static DailyFocusStatisticsResponse from(DailyFocusStatistics statistics) {
        return new DailyFocusStatisticsResponse(
                statistics.hourlyData(),
                statistics.dailyData(),
                statistics.monthlyData()
        );
    }
}
