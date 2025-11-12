package whispy_server.whispy.domain.statistics.focus.summary.application.port.out;

import whispy_server.whispy.domain.statistics.focus.summary.adapter.out.dto.FocusAggregationDto;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.focus.FocusSessionDto;
import whispy_server.whispy.domain.statistics.focus.summary.adapter.out.dto.TagMinutesDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface QueryFocusStatisticsPort {
    List<FocusSessionDto> findByUserIdAndPeriod(Long userId, LocalDateTime start, LocalDateTime end);

    FocusAggregationDto aggregateByPeriod(Long userId, LocalDateTime start, LocalDateTime end);

    int sumMinutesByDate(Long userId, LocalDate date);

    List<TagMinutesDto> aggregateByTag(Long userId, LocalDateTime start, LocalDateTime end);
}
