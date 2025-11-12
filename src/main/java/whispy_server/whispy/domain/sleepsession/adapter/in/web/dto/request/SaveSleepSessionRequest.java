package whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SaveSleepSessionRequest(
        @NotNull LocalDateTime startedAt,
        @NotNull LocalDateTime endedAt,
        @Min(1) int durationSeconds
) { }
