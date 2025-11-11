package whispy_server.whispy.domain.focussession.model;

import whispy_server.whispy.domain.focussession.model.types.FocusTag;
import whispy_server.whispy.global.annotation.Aggregate;
import whispy_server.whispy.global.exception.domain.focussession.FocusSessionDurationExceededException;
import whispy_server.whispy.global.exception.domain.focussession.InvalidFocusSessionDurationException;
import whispy_server.whispy.global.exception.domain.focussession.InvalidFocusSessionTimeRangeException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Aggregate
public record FocusSession(
        Long id,
        Long userId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        int durationSeconds,
        FocusTag tag,
        LocalDateTime createdAt
) {
    public FocusSession {
        if (endedAt.isBefore(startedAt) || endedAt.isEqual(startedAt)) {
            throw InvalidFocusSessionTimeRangeException.EXCEPTION;
        }

        long actualSeconds = ChronoUnit.SECONDS.between(startedAt, endedAt);
        if (durationSeconds > actualSeconds) {
            throw FocusSessionDurationExceededException.EXCEPTION;
        }

        if (durationSeconds <= 0) {
            throw InvalidFocusSessionDurationException.EXCEPTION;
        }
    }
}