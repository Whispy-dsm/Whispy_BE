package whispy_server.whispy.domain.focussession.adapter.in.web.dto.response;

import whispy_server.whispy.domain.focussession.model.FocusSession;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;

import java.time.LocalDateTime;

public record FocusSessionDetailResponse(
        int durationMinutes,
        FocusTag tag,
        LocalDateTime date
) {
    public static FocusSessionDetailResponse from(FocusSession focusSession) {
        return new FocusSessionDetailResponse(
                focusSession.durationSeconds() / 60,
                focusSession.tag(),
                focusSession.startedAt()
        );
    }
}
