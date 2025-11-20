package whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.sleepsession.model.SleepSession;

import java.time.LocalDateTime;

@Schema(description = "수면 세션 응답")
public record SleepSessionResponse(
        @Schema(description = "세션 ID", example = "1")
        Long id,
        @Schema(description = "사용자 ID", example = "1")
        Long userId,
        @Schema(description = "시작 일시", example = "2024-01-01T22:00:00")
        LocalDateTime startedAt,
        @Schema(description = "종료 일시", example = "2024-01-02T06:00:00")
        LocalDateTime endedAt,
        @Schema(description = "지속 시간(초)", example = "28800")
        int durationSeconds,
        @Schema(description = "생성 일시", example = "2024-01-01T22:00:00")
        LocalDateTime createdAt
) {
    public static SleepSessionResponse from(SleepSession sleepSession) {
        return new SleepSessionResponse(
                sleepSession.id(),
                sleepSession.userId(),
                sleepSession.startedAt(),
                sleepSession.endedAt(),
                sleepSession.durationSeconds(),
                sleepSession.createdAt()
        );
    }
}
