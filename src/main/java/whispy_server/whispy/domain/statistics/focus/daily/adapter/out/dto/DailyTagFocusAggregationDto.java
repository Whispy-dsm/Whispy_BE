package whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto;

import whispy_server.whispy.domain.focussession.model.types.FocusTag;

import java.time.LocalDate;

public record DailyTagFocusAggregationDto(
        LocalDate date,
        FocusTag tag,
        int minutes
) {
}
