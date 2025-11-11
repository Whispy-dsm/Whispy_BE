package whispy_server.whispy.domain.statistics.sleep.summary.application.port.out;

import whispy_server.whispy.domain.statistics.sleep.summary.adapter.out.dto.SleepAggregationDto;
import whispy_server.whispy.domain.statistics.sleep.summary.adapter.out.dto.SleepDetailedAggregationDto;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.sleep.SleepSessionDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface QuerySleepStatisticsPort {
    List<SleepSessionDto> findByUserIdAndPeriod(Long userId, LocalDateTime start, LocalDateTime end);

    SleepAggregationDto aggregateByPeriod(Long userId, LocalDateTime start, LocalDateTime end);

    int sumMinutesByDate(Long userId, LocalDate date);

    SleepDetailedAggregationDto aggregateDetailedStatistics(Long userId, LocalDateTime start, LocalDateTime end);
}
