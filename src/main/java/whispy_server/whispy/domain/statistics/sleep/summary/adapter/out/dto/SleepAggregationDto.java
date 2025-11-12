package whispy_server.whispy.domain.statistics.sleep.summary.adapter.out.dto;

public record SleepAggregationDto(
        int totalCount,
        int totalMinutes
) {
}
