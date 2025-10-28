package whispy_server.whispy.domain.statistics.application.port.out;

import whispy_server.whispy.domain.statistics.adapter.out.dto.FocusSessionDto;

import java.time.LocalDateTime;
import java.util.List;

public interface QueryFocusStatisticsPort {
    List<FocusSessionDto> findByUserIdAndPeriod(Long userId, LocalDateTime start, LocalDateTime end);
}
