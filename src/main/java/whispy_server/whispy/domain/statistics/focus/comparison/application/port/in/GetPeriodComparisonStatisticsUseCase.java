package whispy_server.whispy.domain.statistics.focus.comparison.application.port.in;

import whispy_server.whispy.domain.statistics.focus.comparison.adapter.in.web.dto.response.PeriodComparisonResponse;
import whispy_server.whispy.domain.statistics.focus.types.FocusPeriodType;
import whispy_server.whispy.global.annotation.UseCase;

import java.time.LocalDate;

@UseCase
public interface GetPeriodComparisonStatisticsUseCase {
    PeriodComparisonResponse execute(FocusPeriodType period, LocalDate date);
}
