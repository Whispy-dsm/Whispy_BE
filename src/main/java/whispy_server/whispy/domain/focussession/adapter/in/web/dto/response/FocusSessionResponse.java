package whispy_server.whispy.domain.focussession.adapter.in.web.dto.response;

import whispy_server.whispy.domain.focussession.model.FocusSession;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;

import java.time.LocalDateTime;

public record FocusSessionResponse(
        Long id,
        Long userId,
        Long musicId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        int durationSeconds,
        FocusTag tag,
        LocalDateTime createdAt
) {
    public static FocusSessionResponse from(FocusSession focusSession) {
        return new FocusSessionResponse(
                focusSession.id(),
                focusSession.userId(),
                focusSession.musicId(),
                focusSession.startedAt(),
                focusSession.endedAt(),
                focusSession.durationSeconds(),
                focusSession.tag(),
                focusSession.createdAt()
        );
    }
}