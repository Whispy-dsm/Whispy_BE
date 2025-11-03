package whispy_server.whispy.domain.statistics.focus.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.statistics.focus.comparison.adapter.in.web.dto.response.PeriodComparisonResponse;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.in.web.dto.response.DailyFocusStatisticsResponse;
import whispy_server.whispy.domain.statistics.focus.summary.adapter.in.web.dto.response.FocusStatisticsResponse;
import whispy_server.whispy.domain.statistics.focus.daily.application.port.in.GetDailyFocusStatisticsUseCase;
import whispy_server.whispy.domain.statistics.focus.summary.application.port.in.GetFocusStatisticsUseCase;
import whispy_server.whispy.domain.statistics.focus.comparison.application.port.in.GetPeriodComparisonStatisticsUseCase;
import whispy_server.whispy.domain.statistics.focus.types.FocusPeriodType;
import whispy_server.whispy.global.document.api.statistics.StatisticsApiDocument;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
public class FocusStatisticsController implements StatisticsApiDocument {

    private final GetFocusStatisticsUseCase getFocusStatisticsUseCase;
    private final GetPeriodComparisonStatisticsUseCase getPeriodComparisonStatisticsUseCase;
    private final GetDailyFocusStatisticsUseCase getDailyFocusStatisticsUseCase;

    @GetMapping("/focus")
    public FocusStatisticsResponse getFocusStatistics(
            @RequestParam FocusPeriodType period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return getFocusStatisticsUseCase.execute(period, date);
    }

    @GetMapping("/focus/comparison")
    public PeriodComparisonResponse getPeriodComparison(
            @RequestParam FocusPeriodType period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return getPeriodComparisonStatisticsUseCase.execute(period, date);
    }

    @GetMapping("/focus/daily")
    public DailyFocusStatisticsResponse getDailyFocusStatistics(
            @RequestParam FocusPeriodType period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return getDailyFocusStatisticsUseCase.execute(period, date);
    }
}
