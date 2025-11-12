package whispy_server.whispy.domain.statistics.activity.adapter.in.web.dto.response;

import java.time.DayOfWeek;
import java.util.Map;

public record WeeklySessionExistsResponse(
        Map<DayOfWeek, Boolean> weeklyExists
) {
}
