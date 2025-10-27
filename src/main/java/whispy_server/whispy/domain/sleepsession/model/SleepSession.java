package whispy_server.whispy.domain.sleepsession.model;

import whispy_server.whispy.global.annotation.Aggregate;
import whispy_server.whispy.global.exception.domain.sleepsession.InvalidSleepSessionDurationException;
import whispy_server.whispy.global.exception.domain.sleepsession.InvalidSleepSessionTimeRangeException;
import whispy_server.whispy.global.exception.domain.sleepsession.SleepSessionDurationExceededException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Aggregate
public record SleepSession(
        Long id,
        Long userId,
        Long musicId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        int durationSeconds,
        LocalDateTime createdAt
) {
    public SleepSession {
        if (endedAt.isBefore(startedAt) || endedAt.isEqual(startedAt)) {
            throw InvalidSleepSessionTimeRangeException.EXCEPTION;
        }

        long actualSeconds = ChronoUnit.SECONDS.between(startedAt, endedAt);
        if (durationSeconds > actualSeconds) {
            throw SleepSessionDurationExceededException.EXCEPTION;
        }

        if (durationSeconds <= 0) {
            throw InvalidSleepSessionDurationException.EXCEPTION;
        }
    }
}
