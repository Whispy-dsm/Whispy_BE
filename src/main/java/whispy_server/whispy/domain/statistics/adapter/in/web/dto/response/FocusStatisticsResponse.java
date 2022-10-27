package whispy_server.whispy.domain.statistics.adapter.in.web.dto.response;

import whispy_server.whispy.domain.focussession.model.types.FocusTag;
import whispy_server.whispy.domain.statistics.model.ChartDataPoint;
import whispy_server.whispy.domain.statistics.model.FocusStatistics;

import java.util.List;
import java.util.Map;

public record FocusStatisticsResponse(
        int totalCount,
        int totalMinutes,
        int todayMinutes,
        Map<FocusTag, Integer> tagMinutes,
        List<ChartDataPoint> chartData
) {
    public static FocusStatisticsResponse from(FocusStatistics statistics) {
        return new FocusStatisticsResponse(
                statistics.totalCount(),
                statistics.totalMinutes(),
                statistics.todayMinutes(),
                statistics.tagMinutes(),
                statistics.chartData()
        );
    }
}
