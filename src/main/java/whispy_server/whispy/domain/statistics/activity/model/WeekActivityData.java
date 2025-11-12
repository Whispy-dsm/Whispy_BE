package whispy_server.whispy.domain.statistics.activity.model;

import java.util.List;

public record WeekActivityData(
        int weekIndex,
        List<DayActivityData> days
) {
}
