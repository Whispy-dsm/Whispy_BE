package whispy_server.whispy.domain.statistics.sleep.summary.model;

import whispy_server.whispy.global.annotation.Aggregate;
import java.time.LocalTime;

@Aggregate
public record SleepStatistics(
        int todayMinutes,
        int averageMinutes,
        double sleepConsistency,
        LocalTime averageBedTime,
        LocalTime averageWakeTime,
        int totalMinutes
) {
}
