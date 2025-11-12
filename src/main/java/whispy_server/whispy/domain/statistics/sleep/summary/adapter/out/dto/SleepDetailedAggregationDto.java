package whispy_server.whispy.domain.statistics.sleep.summary.adapter.out.dto;

public record SleepDetailedAggregationDto(
        int totalCount,
        int totalMinutes,
        int averageMinutes,
        int averageBedTimeMinutes,
        int averageWakeTimeMinutes
) {
}
