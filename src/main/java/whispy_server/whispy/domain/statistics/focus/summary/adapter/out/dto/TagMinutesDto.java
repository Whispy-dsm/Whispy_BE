package whispy_server.whispy.domain.statistics.focus.summary.adapter.out.dto;

import whispy_server.whispy.domain.focussession.model.types.FocusTag;

public record TagMinutesDto(
        FocusTag tag,
        int minutes
) {
}
