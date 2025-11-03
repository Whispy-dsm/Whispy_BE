package whispy_server.whispy.domain.statistics.sleep.summary.application.port.in;

import whispy_server.whispy.domain.statistics.sleep.summary.adapter.in.web.dto.response.SleepStatisticsResponse;
import whispy_server.whispy.domain.statistics.sleep.types.SleepPeriodType;
import whispy_server.whispy.global.annotation.UseCase;

import java.time.LocalDate;

@UseCase
public interface GetSleepStatisticsUseCase {
    SleepStatisticsResponse execute(SleepPeriodType period, LocalDate date);
}
