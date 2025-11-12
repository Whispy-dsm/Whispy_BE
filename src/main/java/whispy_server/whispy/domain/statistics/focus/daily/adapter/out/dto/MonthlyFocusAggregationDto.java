package whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto;

public record MonthlyFocusAggregationDto(
        int month,
        int totalMinutes
) {
}
