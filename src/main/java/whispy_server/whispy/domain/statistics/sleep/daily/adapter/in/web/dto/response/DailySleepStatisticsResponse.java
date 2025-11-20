package whispy_server.whispy.domain.statistics.sleep.daily.adapter.in.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.statistics.sleep.daily.model.DailySleepData;
import whispy_server.whispy.domain.statistics.sleep.daily.model.DailySleepStatistics;
import whispy_server.whispy.domain.statistics.sleep.daily.model.MonthlySleepData;

import java.util.List;

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
