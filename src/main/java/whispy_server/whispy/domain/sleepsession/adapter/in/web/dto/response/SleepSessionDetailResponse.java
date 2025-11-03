package whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response;

import whispy_server.whispy.domain.sleepsession.model.SleepSession;

import java.time.LocalDateTime;

public record SleepSessionDetailResponse(
        int durationMinutes,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        LocalDateTime createAt
) {
    public static SleepSessionDetailResponse from(SleepSession sleepSession) {
        return new SleepSessionDetailResponse(
                sleepSession.durationSeconds() / 60,
                sleepSession.startedAt(),
                sleepSession.endedAt(),
                sleepSession.createdAt()
        );
    }
}
