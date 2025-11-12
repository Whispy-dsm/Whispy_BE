package whispy_server.whispy.domain.statistics.shared.adapter.out.dto.sleep;

import java.time.LocalDateTime;

public record SleepSessionDto(
        Long id,
        Long userId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        int durationSeconds,
        LocalDateTime createdAt
) {
}
