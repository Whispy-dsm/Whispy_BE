package whispy_server.whispy.domain.statistics.sleep.daily.adapter.out.dto;

public record MonthlySleepAggregationDto(
        int month,
        int totalMinutes
) {
}
