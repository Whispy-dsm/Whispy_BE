package whispy_server.whispy.domain.statistics.focus.adapter.out.dto;

import whispy_server.whispy.domain.focussession.model.types.FocusTag;

import java.time.LocalDateTime;

public record FocusSessionDto(
        Long id,
        Long userId,
        Long musicId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        int durationSeconds,
        FocusTag tag,
        LocalDateTime createdAt
) {
}
