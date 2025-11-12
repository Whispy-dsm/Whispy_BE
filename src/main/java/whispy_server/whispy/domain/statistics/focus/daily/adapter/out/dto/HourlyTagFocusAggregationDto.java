package whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto;

import whispy_server.whispy.domain.focussession.model.types.FocusTag;

public record HourlyTagFocusAggregationDto(
        int hour,
        FocusTag tag,
        int minutes
) {
}
