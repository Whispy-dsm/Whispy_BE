package whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto;

import java.sql.Date;
import java.time.LocalDate;

public record DailyFocusAggregationDto(
        LocalDate date,
        int totalMinutes
) {
    public DailyFocusAggregationDto(Date date, int totalMinutes) {
        this(date.toLocalDate(), totalMinutes);
    }
}
