package whispy_server.whispy.domain.statistics.focus.adapter.in.web.dto.response;

import whispy_server.whispy.domain.focussession.model.types.FocusTag;
import whispy_server.whispy.domain.statistics.focus.model.FocusSessionSummary;
import whispy_server.whispy.domain.statistics.focus.model.FocusStatistics;

import java.util.List;
import java.util.Map;

public record FocusStatisticsResponse(
        int totalCount,
        int totalMinutes,
        int todayMinutes,
        Map<FocusTag, Integer> tagMinutes,
        List<FocusSessionSummary> sessions
) {
    public static FocusStatisticsResponse from(FocusStatistics statistics) {
        return new FocusStatisticsResponse(
                statistics.totalCount(),
                statistics.totalMinutes(),
                statistics.todayMinutes(),
                statistics.tagMinutes(),
                statistics.sessions()
        );
    }
}
