package whispy_server.whispy.domain.statistics.focus.model;

import whispy_server.whispy.domain.focussession.model.types.FocusTag;

import java.time.LocalDateTime;

public record FocusSessionSummary(
        LocalDateTime startedAt,
        int durationMinutes,
        FocusTag tag
) {
}
