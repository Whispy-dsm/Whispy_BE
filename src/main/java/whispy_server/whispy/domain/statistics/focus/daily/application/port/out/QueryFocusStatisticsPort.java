package whispy_server.whispy.domain.statistics.focus.daily.application.port.out;

import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.DailyFocusAggregationDto;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.focus.FocusSessionDto;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.HourlyFocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.MonthlyFocusAggregationDto;

import java.time.LocalDateTime;
import java.util.List;

public interface QueryFocusStatisticsPort {
    List<FocusSessionDto> findByUserIdAndPeriod(Long userId, LocalDateTime start, LocalDateTime end);
    
    List<HourlyFocusAggregationDto> aggregateHourlyMinutes(Long userId, LocalDateTime start, LocalDateTime end);
    
    List<DailyFocusAggregationDto> aggregateDailyMinutes(Long userId, LocalDateTime start, LocalDateTime end);
    
    List<MonthlyFocusAggregationDto> aggregateMonthlyMinutes(Long userId, int year);
}
