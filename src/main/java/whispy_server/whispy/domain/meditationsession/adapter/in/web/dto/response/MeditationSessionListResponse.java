package whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response;

import whispy_server.whispy.domain.meditationsession.model.MeditationSession;
import whispy_server.whispy.domain.meditationsession.model.types.BreatheMode;

import java.time.LocalDateTime;

public record MeditationSessionListResponse(
        Long id,
        int durationMinutes,
        BreatheMode breatheMode,
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
