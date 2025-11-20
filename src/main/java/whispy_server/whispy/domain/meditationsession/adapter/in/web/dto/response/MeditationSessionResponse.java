package whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.meditationsession.model.MeditationSession;
import whispy_server.whispy.domain.meditationsession.model.types.BreatheMode;

import java.time.LocalDateTime;

@Schema(description = "명상 세션 응답")
public record MeditationSessionResponse(
        @Schema(description = "세션 ID", example = "1")
        Long id,
        @Schema(description = "사용자 ID", example = "1")
        Long userId,
        @Schema(description = "시작 일시", example = "2024-01-01T10:00:00")
        LocalDateTime startedAt,
        @Schema(description = "종료 일시", example = "2024-01-01T10:30:00")
        LocalDateTime endedAt,
        @Schema(description = "지속 시간(초)", example = "1800")
        int durationSeconds,
        @Schema(description = "호흡 모드")
        BreatheMode breatheMode,
        @Schema(description = "생성 일시", example = "2024-01-01T10:00:00")
        LocalDateTime createdAt,
        @Schema(description = "오늘 총 명상 시간(분)", example = "60")
        int todayTotalMinutes
) {
    public static MeditationSessionResponse from(MeditationSession meditationSession, int todayTotalMinutes) {
        return new MeditationSessionResponse(
                meditationSession.id(),
                meditationSession.userId(),
                meditationSession.startedAt(),
                meditationSession.endedAt(),
                meditationSession.durationSeconds(),
                meditationSession.breatheMode(),
                meditationSession.createdAt(),
                todayTotalMinutes
        );
    }
}
