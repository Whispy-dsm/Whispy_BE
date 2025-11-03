package whispy_server.whispy.domain.statistics.focus.daily.model;

import java.time.DayOfWeek;
import java.time.LocalDate;

public record DailyFocusData(
        LocalDate date,
        DayOfWeek dayOfWeek,
        int day,
        int minutes
) {
}
