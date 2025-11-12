package whispy_server.whispy.domain.meditationsession.model;

import whispy_server.whispy.domain.meditationsession.model.types.BreatheMode;
import whispy_server.whispy.global.annotation.Aggregate;
import whispy_server.whispy.global.exception.domain.meditationsession.InvalidMeditationSessionDurationException;
import whispy_server.whispy.global.exception.domain.meditationsession.InvalidMeditationSessionTimeRangeException;
import whispy_server.whispy.global.exception.domain.meditationsession.MeditationSessionDurationExceededException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Aggregate
public record MeditationSession(
        Long id,
        Long userId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        int durationSeconds,
        BreatheMode breatheMode,
        LocalDateTime createdAt
) {
    public MeditationSession {
        if (endedAt.isBefore(startedAt) || endedAt.isEqual(startedAt)) {
            throw InvalidMeditationSessionTimeRangeException.EXCEPTION;
        }

        long actualSeconds = ChronoUnit.SECONDS.between(startedAt, endedAt);
        if (durationSeconds > actualSeconds) {
            throw MeditationSessionDurationExceededException.EXCEPTION;
        }

        if (durationSeconds <= 0) {
            throw InvalidMeditationSessionDurationException.EXCEPTION;
        }
    }
}
