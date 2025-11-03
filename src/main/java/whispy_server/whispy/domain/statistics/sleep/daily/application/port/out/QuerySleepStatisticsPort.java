package whispy_server.whispy.domain.statistics.sleep.daily.application.port.out;

import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.sleep.SleepSessionDto;

import java.time.LocalDateTime;
import java.util.List;

public interface QuerySleepStatisticsPort {
    List<SleepSessionDto> findByUserIdAndPeriod(Long userId, LocalDateTime start, LocalDateTime end);
}
