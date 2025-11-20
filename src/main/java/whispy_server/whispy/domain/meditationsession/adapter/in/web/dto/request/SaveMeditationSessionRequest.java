package whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import whispy_server.whispy.domain.meditationsession.model.types.BreatheMode;

import java.time.LocalDateTime;

@Schema(description = "명상 세션 저장 요청")
public record SaveMeditationSessionRequest(
        @Schema(description = "시작 일시", example = "2024-01-01T10:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        LocalDateTime startedAt,
        @Schema(description = "종료 일시", example = "2024-01-01T10:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        LocalDateTime endedAt,
        @Schema(description = "지속 시간(초)", example = "1800", requiredMode = Schema.RequiredMode.REQUIRED)
        @Min(1)
        int durationSeconds,
        @Schema(description = "호흡 모드", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        BreatheMode breatheMode
) { }
