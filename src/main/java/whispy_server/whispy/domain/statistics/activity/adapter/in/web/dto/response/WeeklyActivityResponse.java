package whispy_server.whispy.domain.statistics.activity.adapter.in.web.dto.response;

import whispy_server.whispy.domain.statistics.activity.model.MonthIndicator;
import whispy_server.whispy.domain.statistics.activity.model.WeekActivityData;

import java.time.LocalDate;
import java.util.List;

public record WeeklyActivityResponse(
        LocalDate startDate,
        LocalDate endDate,
        List<MonthIndicator> months,
        List<WeekActivityData> weeks
) {
}
