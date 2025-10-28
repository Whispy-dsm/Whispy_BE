package whispy_server.whispy.domain.statistics.application.port.in;

import whispy_server.whispy.domain.statistics.adapter.in.web.dto.response.FocusStatisticsResponse;
import whispy_server.whispy.domain.statistics.model.types.PeriodType;

import java.time.LocalDate;

public interface GetFocusStatisticsUseCase {
    FocusStatisticsResponse execute(PeriodType period, LocalDate date);
}

