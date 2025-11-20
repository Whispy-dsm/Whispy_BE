package whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.meditationsession.model.MeditationSession;
import whispy_server.whispy.domain.meditationsession.model.types.BreatheMode;

import java.time.LocalDateTime;

@Schema(description = "명상 세션 상세 응답")
public record MeditationSessionDetailResponse(
        @Schema(description = "지속 시간(분)", example = "30")
        int durationMinutes,
        @Schema(description = "호흡 모드")
        BreatheMode breatheMode,
        @Schema(description = "일시", example = "2024-01-01T10:00:00")
        LocalDateTime date
) {
    public static MeditationSessionDetailResponse from(MeditationSession meditationSession) {
        return new MeditationSessionDetailResponse(
                meditationSession.durationSeconds() / 60,
                meditationSession.breatheMode(),
                meditationSession.startedAt()
        );
    }
}
