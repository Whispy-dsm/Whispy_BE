package whispy_server.whispy.domain.statistics.activity.applicatoin.port.out;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public interface QueryActivityMinutesPort {
    Map<LocalDate, Integer> findSessionMinutesInPeriod(Long userId, LocalDateTime start, LocalDateTime end);
}
