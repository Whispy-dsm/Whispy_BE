package whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto;

public record HourlyFocusAggregationDto(
        int hour,
        int totalMinutes
) {
}
