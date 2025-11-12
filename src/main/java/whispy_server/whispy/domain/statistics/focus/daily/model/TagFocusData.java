package whispy_server.whispy.domain.statistics.focus.daily.model;

import whispy_server.whispy.domain.focussession.model.types.FocusTag;

public record TagFocusData(
        FocusTag tag,
        int minutes
) {
}
