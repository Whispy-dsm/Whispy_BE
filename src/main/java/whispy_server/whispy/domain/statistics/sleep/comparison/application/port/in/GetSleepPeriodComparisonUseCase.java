package whispy_server.whispy.domain.statistics.sleep.comparison.application.port.in;

import whispy_server.whispy.domain.statistics.sleep.comparison.adapter.in.web.dto.response.SleepPeriodComparisonResponse;
import whispy_server.whispy.domain.statistics.sleep.types.SleepPeriodType;
import whispy_server.whispy.global.annotation.UseCase;

import java.time.LocalDate;

@UseCase
public interface GetSleepPeriodComparisonUseCase {
    SleepPeriodComparisonResponse execute(SleepPeriodType period, LocalDate date);
}
