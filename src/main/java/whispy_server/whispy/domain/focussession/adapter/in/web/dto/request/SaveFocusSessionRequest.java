package whispy_server.whispy.domain.focussession.adapter.in.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;

import java.time.LocalDateTime;

public record SaveFocusSessionRequest(
        @NotNull Long musicId,
        @NotNull LocalDateTime startedAt,
        @NotNull LocalDateTime endedAt,
        @Min(1) int durationSeconds,
        @NotNull FocusTag tag
) { }