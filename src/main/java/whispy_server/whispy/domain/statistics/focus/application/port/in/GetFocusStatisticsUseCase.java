package whispy_server.whispy.domain.statistics.focus.application.port.in;

import whispy_server.whispy.domain.statistics.focus.adapter.in.web.dto.response.FocusStatisticsResponse;
import whispy_server.whispy.domain.statistics.focus.model.types.PeriodType;
import whispy_server.whispy.global.annotation.UseCase;

import java.time.LocalDate;

@UseCase
public interface GetFocusStatisticsUseCase {
    FocusStatisticsResponse execute(PeriodType period, LocalDate date);
}

