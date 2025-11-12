package whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto;

import whispy_server.whispy.domain.focussession.model.types.FocusTag;

public record MonthlyTagFocusAggregationDto(
        int month,
        FocusTag tag,
        int minutes
) {
}
