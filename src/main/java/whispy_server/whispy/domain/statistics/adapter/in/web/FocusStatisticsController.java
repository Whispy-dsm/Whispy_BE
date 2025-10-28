package whispy_server.whispy.domain.statistics.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.statistics.adapter.in.web.dto.response.FocusStatisticsResponse;
import whispy_server.whispy.domain.statistics.application.port.in.GetFocusStatisticsUseCase;
import whispy_server.whispy.domain.statistics.model.types.PeriodType;
import whispy_server.whispy.global.document.api.statistics.StatisticsApiDocument;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
public class FocusStatisticsController implements StatisticsApiDocument {

    private final GetFocusStatisticsUseCase getFocusStatisticsUseCase;

    @GetMapping("/focus")
    public FocusStatisticsResponse getFocusStatistics(
            @RequestParam PeriodType period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return getFocusStatisticsUseCase.execute(period, date);
    }
}
