package whispy_server.whispy.domain.statistics.focus.daily.application.port.out;

import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.*;
import whispy_server.whispy.domain.statistics.focus.summary.adapter.out.dto.TagMinutesDto;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.focus.FocusSessionDto;

import java.time.LocalDateTime;
import java.util.List;

public interface QueryFocusStatisticsPort {
    List<FocusSessionDto> findByUserIdAndPeriod(Long userId, LocalDateTime start, LocalDateTime end);

    List<HourlyFocusAggregationDto> aggregateHourlyMinutes(Long userId, LocalDateTime start, LocalDateTime end);

    List<DailyFocusAggregationDto> aggregateDailyMinutes(Long userId, LocalDateTime start, LocalDateTime end);

    List<MonthlyFocusAggregationDto> aggregateMonthlyMinutes(Long userId, int year);

    int getTotalMinutes(Long userId, LocalDateTime start, LocalDateTime end);

    List<TagMinutesDto> aggregateByTag(Long userId, LocalDateTime start, LocalDateTime end);

    List<HourlyTagFocusAggregationDto> aggregateHourlyByTag(Long userId, LocalDateTime start, LocalDateTime end);

    List<DailyTagFocusAggregationDto> aggregateDailyByTag(Long userId, LocalDateTime start, LocalDateTime end);

    List<MonthlyTagFocusAggregationDto> aggregateMonthlyByTag(Long userId, int year);
}
