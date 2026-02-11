package whispy_server.whispy.domain.statistics.sleep.daily.adapter.out.dto;

import java.sql.Date;
import java.time.LocalDate;

public record DailySleepAggregationDto(
        LocalDate date,
        int totalMinutes
) {
    public DailySleepAggregationDto(Date date, int totalMinutes) {
        this(date.toLocalDate(), totalMinutes);
    }
}
