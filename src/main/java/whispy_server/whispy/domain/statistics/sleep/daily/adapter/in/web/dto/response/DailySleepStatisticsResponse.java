package whispy_server.whispy.domain.statistics.sleep.daily.adapter.in.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.statistics.sleep.daily.model.DailySleepData;
import whispy_server.whispy.domain.statistics.sleep.daily.model.DailySleepStatistics;
import whispy_server.whispy.domain.statistics.sleep.daily.model.MonthlySleepData;

import java.util.List;

/**
 * 일일 수면 통계 응답 DTO.
 *
 * 수면 통계의 일별, 월별 상세 데이터를 포함합니다.
 *
 * @param dailyData 일별 수면 데이터 목록
 * @param monthlyData 월별 수면 데이터 목록
 */
@Schema(description = "일별 수면 통계 응답")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DailySleepStatisticsResponse(
        @Schema(description = "일별 수면 데이터")
        List<DailySleepData> dailyData,
        @Schema(description = "월별 수면 데이터")
        List<MonthlySleepData> monthlyData
) {
    public static DailySleepStatisticsResponse from(DailySleepStatistics statistics) {
        return new DailySleepStatisticsResponse(
                statistics.dailyData(),
                statistics.monthlyData()
        );
    }
}
