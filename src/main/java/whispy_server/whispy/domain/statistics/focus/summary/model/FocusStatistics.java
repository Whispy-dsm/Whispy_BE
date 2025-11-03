package whispy_server.whispy.domain.statistics.focus.summary.model;

import whispy_server.whispy.domain.focussession.model.types.FocusTag;
import whispy_server.whispy.global.annotation.Aggregate;

import java.util.Map;

@Aggregate
public record FocusStatistics(
        int totalCount,
        int totalMinutes,
        int todayMinutes,
        Map<FocusTag, Integer> tagMinutes
) {
}
