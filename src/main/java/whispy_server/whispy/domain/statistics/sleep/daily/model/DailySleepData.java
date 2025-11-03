package whispy_server.whispy.domain.statistics.sleep.daily.model;

import java.time.DayOfWeek;
import java.time.LocalDate;

public record DailySleepData(
        LocalDate date,
        DayOfWeek dayOfWeek,
        int day,
        int minutes
) {
}
