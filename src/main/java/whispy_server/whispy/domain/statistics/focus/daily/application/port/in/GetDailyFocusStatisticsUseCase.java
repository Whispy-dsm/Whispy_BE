package whispy_server.whispy.domain.statistics.focus.daily.application.port.in;

import whispy_server.whispy.domain.statistics.focus.daily.adapter.in.web.dto.response.DailyFocusStatisticsResponse;
import whispy_server.whispy.domain.statistics.focus.types.FocusPeriodType;
import whispy_server.whispy.global.annotation.UseCase;
import java.time.LocalDate;

@UseCase
public interface GetDailyFocusStatisticsUseCase {
    DailyFocusStatisticsResponse execute(FocusPeriodType period, LocalDate date);
}
