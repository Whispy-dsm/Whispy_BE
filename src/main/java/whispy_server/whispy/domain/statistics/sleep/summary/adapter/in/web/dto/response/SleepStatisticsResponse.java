package whispy_server.whispy.domain.statistics.sleep.summary.adapter.in.web.dto.response;

import whispy_server.whispy.domain.statistics.sleep.summary.model.SleepStatistics;
import java.time.LocalTime;

public record SleepStatisticsResponse(
        int todayMinutes,
        int averageMinutes,
        double sleepConsistency,
        LocalTime averageBedTime,
        LocalTime averageWakeTime,
        int totalMinutes
) {
    public static SleepStatisticsResponse from(SleepStatistics statistics) {
        return new SleepStatisticsResponse(
                statistics.todayMinutes(),
                statistics.averageMinutes(),
                statistics.sleepConsistency(),
                statistics.averageBedTime(),
                statistics.averageWakeTime(),
                statistics.totalMinutes()
        );
    }
}
