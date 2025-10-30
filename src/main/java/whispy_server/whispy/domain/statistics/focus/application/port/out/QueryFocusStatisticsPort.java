package whispy_server.whispy.domain.statistics.focus.application.port.out;

import whispy_server.whispy.domain.statistics.focus.adapter.out.dto.FocusSessionDto;

import java.time.LocalDateTime;
import java.util.List;

public interface QueryFocusStatisticsPort {
    List<FocusSessionDto> findByUserIdAndPeriod(Long userId, LocalDateTime start, LocalDateTime end);
}
