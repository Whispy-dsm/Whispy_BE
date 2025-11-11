package whispy_server.whispy.domain.statistics.meditation.daily.application.port.out;

import java.time.LocalDateTime;

public interface QueryMeditationStatisticsPort {
    int getTotalMinutes(Long userId, LocalDateTime start, LocalDateTime end);
}
