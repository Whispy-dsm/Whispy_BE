package whispy_server.whispy.domain.statistics.activity.model;

import java.time.Month;

public record MonthIndicator(
        int year,
        int month,
        Month monthName,
        int startWeekIndex
) {
}
