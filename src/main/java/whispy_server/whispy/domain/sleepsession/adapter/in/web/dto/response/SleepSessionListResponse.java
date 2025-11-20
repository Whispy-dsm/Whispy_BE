package whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.sleepsession.model.SleepSession;

import java.time.LocalDateTime;

@Schema(description = "수면 세션 목록 응답")
public record SleepSessionListResponse(
        @Schema(description = "세션 ID", example = "1")
        Long id,
        @Schema(description = "시작 일시", example = "2024-01-01T22:00:00")
        LocalDateTime startedAt,
        @Schema(description = "지속 시간(분)", example = "480")
        int durationMinutes
) {
    public static SleepSessionListResponse from(SleepSession sleepSession) {
        return new SleepSessionListResponse(
                sleepSession.id(),
                sleepSession.startedAt(),
                sleepSession.durationSeconds() / 60
        );
    }

}
