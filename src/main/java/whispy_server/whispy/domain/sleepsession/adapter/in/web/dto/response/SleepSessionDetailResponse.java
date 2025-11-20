package whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.sleepsession.model.SleepSession;

import java.time.LocalDateTime;

@Schema(description = "수면 세션 상세 응답")
public record SleepSessionDetailResponse(
        @Schema(description = "지속 시간(분)", example = "480")
        int durationMinutes,
        @Schema(description = "시작 일시", example = "2024-01-01T22:00:00")
        LocalDateTime startedAt,
        @Schema(description = "종료 일시", example = "2024-01-02T06:00:00")
        LocalDateTime endedAt,
        @Schema(description = "생성 일시", example = "2024-01-01T22:00:00")
        LocalDateTime createAt
) {
    public static SleepSessionDetailResponse from(SleepSession sleepSession) {
        return new SleepSessionDetailResponse(
                sleepSession.durationSeconds() / 60,
                sleepSession.startedAt(),
                sleepSession.endedAt(),
                sleepSession.createdAt()
        );
    }
}
