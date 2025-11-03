package whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response;

import whispy_server.whispy.domain.sleepsession.model.SleepSession;

import java.time.LocalDateTime;

public record SleepSessionListResponse(
        Long id,
        LocalDateTime startedAt,
        int durationMinutes
) {
    public static SleepSessionListResponse from(SleepSession sleepSession) {
        return new SleepSessionListResponse(
                sleepSession.id(),
                sleepSession.startedAt(),
                sleepSession.durationSeconds() / 60
        );
    }

}
