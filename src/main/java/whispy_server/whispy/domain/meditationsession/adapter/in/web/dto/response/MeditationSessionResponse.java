package whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response;

import whispy_server.whispy.domain.meditationsession.model.MeditationSession;
import whispy_server.whispy.domain.meditationsession.model.types.BreatheMode;

import java.time.LocalDateTime;

public record MeditationSessionResponse(
        Long id,
        Long userId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        int durationSeconds,
        BreatheMode breatheMode,
        LocalDateTime createdAt,
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
