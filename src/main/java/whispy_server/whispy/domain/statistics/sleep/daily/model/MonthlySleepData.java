package whispy_server.whispy.domain.statistics.sleep.daily.model;

import java.time.Month;

public record MonthlySleepData(
        int month,
        Month monthName,
        int minutes
) {
}
