package whispy_server.whispy.domain.focussession.adapter.in.web.dto.response;

import whispy_server.whispy.domain.focussession.model.FocusSession;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;

import java.time.LocalDateTime;

public record FocusSessionListResponse(
        Long id,
        int durationMinutes,
        FocusTag tag,
        LocalDateTime startedAt

) {
    public static FocusSessionListResponse from(FocusSession focusSession) {
        return new FocusSessionListResponse(
                focusSession.id(),
                focusSession.durationSeconds() / 60,
                focusSession.tag(),
                focusSession.startedAt()
        );
    }
}
