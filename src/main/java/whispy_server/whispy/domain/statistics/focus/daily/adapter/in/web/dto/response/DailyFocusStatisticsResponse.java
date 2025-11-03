package whispy_server.whispy.domain.statistics.focus.daily.adapter.in.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import whispy_server.whispy.domain.statistics.focus.daily.model.DailyFocusData;
import whispy_server.whispy.domain.statistics.focus.daily.model.DailyFocusStatistics;
import whispy_server.whispy.domain.statistics.focus.daily.model.HourlyFocusData;
import whispy_server.whispy.domain.statistics.focus.daily.model.MonthlyFocusData;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DailyFocusStatisticsResponse(
        List<HourlyFocusData> hourlyData,
        List<DailyFocusData> dailyData,
        List<MonthlyFocusData> monthlyData
) {
    public static DailyFocusStatisticsResponse from(DailyFocusStatistics statistics) {
        return new DailyFocusStatisticsResponse(
                statistics.hourlyData(),
                statistics.dailyData(),
                statistics.monthlyData()
        );
    }
}
