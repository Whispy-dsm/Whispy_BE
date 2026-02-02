package whispy_server.whispy.domain.statistics.sleep.daily.model;

import whispy_server.whispy.global.annotation.Aggregate;
import java.util.List;

/**
 * 일일 수면 통계 애그리게잇.
 *
 * 수면 통계의 일일 또는 월간 상세 데이터를 나타냅니다.
 *
 * @param dailyData 일일 수면 데이터 목록
 * @param monthlyData 월별 수면 데이터 목록
 */
@Aggregate
public record DailySleepStatistics(
        List<DailySleepData> dailyData,
        List<MonthlySleepData> monthlyData
) {
    /**
     * 일별 수면 통계를 생성합니다.
     *
     * @param dailyData 일별 수면 데이터 목록
     * @return 일별 수면 통계
     */
    public static DailySleepStatistics ofDaily(List<DailySleepData> dailyData) {
        return new DailySleepStatistics(dailyData, null);
    }

    /**
     * 월별 수면 통계를 생성합니다.
     *
     * @param monthlyData 월별 수면 데이터 목록 (1~12월)
     * @return 월별 수면 통계
     */
    public static DailySleepStatistics ofMonthly(List<MonthlySleepData> monthlyData) {
        return new DailySleepStatistics(null, monthlyData);
    }
}
