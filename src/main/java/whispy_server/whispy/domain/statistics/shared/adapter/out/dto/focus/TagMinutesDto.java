package whispy_server.whispy.domain.statistics.shared.adapter.out.dto.focus;

import whispy_server.whispy.domain.focussession.model.types.FocusTag;

public record TagMinutesDto(
        FocusTag tag,
        int minutes
) {
}
