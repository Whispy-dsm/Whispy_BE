package whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto;

import java.time.LocalDate;

public record DailyFocusAggregationDto(
        LocalDate date,
        int totalMinutes
) {
}
