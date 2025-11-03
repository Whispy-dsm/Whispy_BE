package whispy_server.whispy.domain.statistics.focus.daily.model;

import java.time.Month;

public record MonthlyFocusData(
        int month,
        Month monthName,
        int minutes
) {
}
