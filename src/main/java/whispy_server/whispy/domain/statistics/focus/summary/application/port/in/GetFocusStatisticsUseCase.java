package whispy_server.whispy.domain.statistics.focus.summary.application.port.in;

import whispy_server.whispy.domain.statistics.focus.summary.adapter.in.web.dto.response.FocusStatisticsResponse;
import whispy_server.whispy.domain.statistics.focus.types.FocusPeriodType;
import whispy_server.whispy.global.annotation.UseCase;

import java.time.LocalDate;

@UseCase
public interface GetFocusStatisticsUseCase {
    FocusStatisticsResponse execute(FocusPeriodType period, LocalDate date);
}
