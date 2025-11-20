package whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "수면 세션 저장 요청")
public record SaveSleepSessionRequest(
        @Schema(description = "시작 일시", example = "2024-01-01T22:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull LocalDateTime startedAt,
        @Schema(description = "종료 일시", example = "2024-01-02T06:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull LocalDateTime endedAt,
        @Schema(description = "지속 시간(초)", example = "28800", requiredMode = Schema.RequiredMode.REQUIRED)
        @Min(1) int durationSeconds
) { }
