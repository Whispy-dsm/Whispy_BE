package whispy_server.whispy.domain.statistics.sleep.daily.application.port.in;

import whispy_server.whispy.domain.statistics.sleep.daily.adapter.in.web.dto.response.DailySleepStatisticsResponse;
import whispy_server.whispy.domain.statistics.sleep.types.SleepPeriodType;
import whispy_server.whispy.global.annotation.UseCase;

import java.time.LocalDate;

@UseCase
public interface GetDailySleepStatisticsUseCase {
    DailySleepStatisticsResponse execute(SleepPeriodType period, LocalDate date);
}
