package whispy_server.whispy.domain.statistics.focus.daily.model;

import whispy_server.whispy.global.annotation.Aggregate;
import java.util.List;

@Aggregate
public record DailyFocusStatistics(
        List<HourlyFocusData> hourlyData,
        List<DailyFocusData> dailyData,
        List<MonthlyFocusData> monthlyData
) {
    public static DailyFocusStatistics ofHourly(List<HourlyFocusData> hourlyData) {
        return new DailyFocusStatistics(hourlyData, null, null);
    }
    
    public static DailyFocusStatistics ofDaily(List<DailyFocusData> dailyData) {
        return new DailyFocusStatistics(null, dailyData, null);
    }
    
    public static DailyFocusStatistics ofMonthly(List<MonthlyFocusData> monthlyData) {
        return new DailyFocusStatistics(null, null, monthlyData);
    }
}
