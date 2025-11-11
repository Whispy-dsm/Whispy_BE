package whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import whispy_server.whispy.domain.meditationsession.model.types.BreatheMode;

import java.time.LocalDateTime;

public record SaveMeditationSessionRequest(
        @NotNull
        LocalDateTime startedAt,
        @NotNull
        LocalDateTime endedAt,
        @Min(1)
        int durationSeconds,
        @NotNull
        BreatheMode breatheMode
) { }
