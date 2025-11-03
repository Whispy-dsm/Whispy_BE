package whispy_server.whispy.domain.statistics.sleep.daily.adapter.in.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import whispy_server.whispy.domain.statistics.sleep.daily.model.DailySleepData;
import whispy_server.whispy.domain.statistics.sleep.daily.model.DailySleepStatistics;
import whispy_server.whispy.domain.statistics.sleep.daily.model.MonthlySleepData;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DailySleepStatisticsResponse(
        List<DailySleepData> dailyData,
        List<MonthlySleepData> monthlyData
) {
    public static DailySleepStatisticsResponse from(DailySleepStatistics statistics) {
        return new DailySleepStatisticsResponse(
                statistics.dailyData(),
                statistics.monthlyData()
        );
    }
}
