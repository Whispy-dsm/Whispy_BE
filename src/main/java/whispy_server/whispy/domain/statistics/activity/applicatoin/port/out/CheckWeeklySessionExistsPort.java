package whispy_server.whispy.domain.statistics.activity.applicatoin.port.out;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public interface CheckWeeklySessionExistsPort {
    Set<LocalDate> findSessionDatesInPeriod(Long userId, LocalDateTime start, LocalDateTime end);
}
