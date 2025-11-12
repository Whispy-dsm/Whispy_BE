package whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response;

import whispy_server.whispy.domain.meditationsession.model.MeditationSession;
import whispy_server.whispy.domain.meditationsession.model.types.BreatheMode;

import java.time.LocalDateTime;

public record MeditationSessionDetailResponse(
        int durationMinutes,
        BreatheMode breatheMode,
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
