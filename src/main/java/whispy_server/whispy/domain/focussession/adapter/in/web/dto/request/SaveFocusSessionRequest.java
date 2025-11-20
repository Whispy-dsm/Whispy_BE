package whispy_server.whispy.domain.focussession.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;

import java.time.LocalDateTime;

@Schema(description = "집중 세션 저장 요청")
public record SaveFocusSessionRequest(
        @Schema(description = "시작 일시", example = "2024-01-01T10:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull LocalDateTime startedAt,
        @Schema(description = "종료 일시", example = "2024-01-01T11:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull LocalDateTime endedAt,
        @Schema(description = "지속 시간(초)", example = "3600", requiredMode = Schema.RequiredMode.REQUIRED)
        @Min(1) int durationSeconds,
        @Schema(description = "집중 태그", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull FocusTag tag
) { }