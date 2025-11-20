package whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.meditationsession.model.MeditationSession;
import whispy_server.whispy.domain.meditationsession.model.types.BreatheMode;

import java.time.LocalDateTime;

@Schema(description = "명상 세션 목록 응답")
public record MeditationSessionListResponse(
        @Schema(description = "세션 ID", example = "1")
        Long id,
        @Schema(description = "지속 시간(분)", example = "30")
        int durationMinutes,
        @Schema(description = "호흡 모드")
        BreatheMode breatheMode,
        @Schema(description = "시작 일시", example = "2024-01-01T10:00:00")
        LocalDateTime startedAt

) {
    public static MeditationSessionListResponse from(MeditationSession meditationSession) {
        return new MeditationSessionListResponse(
                meditationSession.id(),
                meditationSession.durationSeconds() / 60,
                meditationSession.breatheMode(),
                meditationSession.startedAt()
        );
    }
}
