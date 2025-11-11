package whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response;

import whispy_server.whispy.domain.sleepsession.model.SleepSession;

import java.time.LocalDateTime;

public record SleepSessionResponse(
        Long id,
        Long userId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        int durationSeconds,
        LocalDateTime createdAt
) {
    public static SleepSessionResponse from(SleepSession sleepSession) {
        return new SleepSessionResponse(
                sleepSession.id(),
                sleepSession.userId(),
                sleepSession.startedAt(),
                sleepSession.endedAt(),
                sleepSession.durationSeconds(),
                sleepSession.createdAt()
        );
    }
}
