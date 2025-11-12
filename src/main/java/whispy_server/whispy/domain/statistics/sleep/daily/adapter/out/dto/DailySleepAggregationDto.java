package whispy_server.whispy.domain.statistics.sleep.daily.adapter.out.dto;

import java.time.LocalDate;

public record DailySleepAggregationDto(
        LocalDate date,
        int totalMinutes
) {
}
