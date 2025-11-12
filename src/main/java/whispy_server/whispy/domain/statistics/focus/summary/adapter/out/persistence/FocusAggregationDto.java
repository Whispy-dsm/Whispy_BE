package whispy_server.whispy.domain.statistics.focus.summary.adapter.out.persistence;

public record FocusAggregationDto(
        int totalCount,
        int totalMinutes
) {
}
