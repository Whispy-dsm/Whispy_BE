package whispy_server.whispy.domain.statistics.focus.daily.model;

import whispy_server.whispy.global.annotation.Aggregate;
import java.util.List;

/**
 * 일일 집중 통계 애그리게잇.
 *
 * 집중 통계의 시간별, 일별, 월별 상세 데이터를 나타냅니다.
 *
 * @param hourlyData 시간별 집중 데이터 목록
 * @param dailyData 일일 집중 데이터 목록
 * @param monthlyData 월별 집중 데이터 목록
 */
@Aggregate
public record DailyFocusStatistics(
        List<HourlyFocusData> hourlyData,
        List<DailyFocusData> dailyData,
        List<MonthlyFocusData> monthlyData
) {
    /**
     * 시간별 집중 통계를 생성합니다.
     *
     * @param hourlyData 시간별 집중 데이터 목록 (0~23시)
     * @return 시간별 집중 통계
     */
    public static DailyFocusStatistics ofHourly(List<HourlyFocusData> hourlyData) {
        return new DailyFocusStatistics(hourlyData, null, null);
    }

    /**
     * 일별 집중 통계를 생성합니다.
     *
     * @param dailyData 일별 집중 데이터 목록
     * @return 일별 집중 통계
     */
    public static DailyFocusStatistics ofDaily(List<DailyFocusData> dailyData) {
        return new DailyFocusStatistics(null, dailyData, null);
    }

    /**
     * 월별 집중 통계를 생성합니다.
     *
     * @param monthlyData 월별 집중 데이터 목록 (1~12월)
     * @return 월별 집중 통계
     */
    public static DailyFocusStatistics ofMonthly(List<MonthlyFocusData> monthlyData) {
        return new DailyFocusStatistics(null, null, monthlyData);
    }
}
