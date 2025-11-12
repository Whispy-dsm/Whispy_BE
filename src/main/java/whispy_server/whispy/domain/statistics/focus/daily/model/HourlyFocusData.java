package whispy_server.whispy.domain.statistics.focus.daily.model;

import java.util.List;

public record HourlyFocusData(
        int hour,
        int minutes,
        List<TagFocusData> tagData
) {
}
