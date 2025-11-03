package whispy_server.whispy.domain.statistics.sleep.daily.model;

import whispy_server.whispy.global.annotation.Aggregate;
import java.util.List;

@Aggregate
public record DailySleepStatistics(
        List<DailySleepData> dailyData,
        List<MonthlySleepData> monthlyData
) {
    public static DailySleepStatistics ofDaily(List<DailySleepData> dailyData) {
        return new DailySleepStatistics(dailyData, null);
    }
    
    public static DailySleepStatistics ofMonthly(List<MonthlySleepData> monthlyData) {
        return new DailySleepStatistics(null, monthlyData);
    }
}
