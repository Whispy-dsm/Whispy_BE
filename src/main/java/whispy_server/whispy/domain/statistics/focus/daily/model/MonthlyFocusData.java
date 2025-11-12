package whispy_server.whispy.domain.statistics.focus.daily.model;

import java.time.Month;
import java.util.List;

public record MonthlyFocusData(
        int month,
        Month monthName,
        int minutes,
        List<TagFocusData> tagData
) {
}
