package whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto;

import whispy_server.whispy.domain.focussession.model.types.FocusTag;

import java.sql.Date;
import java.time.LocalDate;

public record DailyTagFocusAggregationDto(
        LocalDate date,
        FocusTag tag,
        int minutes
) {
    public DailyTagFocusAggregationDto(Date date, FocusTag tag, int minutes) {
        this(date.toLocalDate(), tag, minutes);
    }
}
