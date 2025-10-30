package whispy_server.whispy.domain.statistics.application.port.in;

import whispy_server.whispy.domain.statistics.adapter.in.web.dto.response.PeriodComparisonResponse;
import whispy_server.whispy.domain.statistics.model.types.PeriodType;
import whispy_server.whispy.global.annotation.UseCase;

import java.time.LocalDate;

@UseCase
public interface GetPeriodComparisonStatisticsUseCase {
    PeriodComparisonResponse execute(PeriodType period, LocalDate date);
}
