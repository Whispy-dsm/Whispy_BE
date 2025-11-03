package whispy_server.whispy.domain.statistics.focus.comparison.application.port.out;

import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.focus.FocusSessionDto;

import java.time.LocalDateTime;
import java.util.List;

public interface QueryFocusComparisonPort {
    List<FocusSessionDto> findByUserIdAndPeriod(Long userId, LocalDateTime start, LocalDateTime end);
}
