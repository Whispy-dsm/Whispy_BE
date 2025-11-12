package whispy_server.whispy.domain.statistics.activity.model;

import java.time.DayOfWeek;
import java.time.LocalDate;

public record DayActivityData(
        LocalDate date,
        DayOfWeek dayOfWeek,
        int totalMinutes
) {
}
