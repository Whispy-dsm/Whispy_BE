package whispy_server.whispy.domain.statistics.model;

import java.time.LocalDateTime;

public record ChartDataPoint(
        String label,
        int minutes,
        LocalDateTime date
) {
}
