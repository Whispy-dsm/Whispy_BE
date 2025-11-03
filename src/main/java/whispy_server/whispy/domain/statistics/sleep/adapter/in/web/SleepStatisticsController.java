package whispy_server.whispy.domain.statistics.sleep.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.statistics.sleep.daily.adapter.in.web.dto.response.DailySleepStatisticsResponse;
import whispy_server.whispy.domain.statistics.sleep.comparison.adapter.in.web.dto.response.SleepPeriodComparisonResponse;
import whispy_server.whispy.domain.statistics.sleep.summary.adapter.in.web.dto.response.SleepStatisticsResponse;
import whispy_server.whispy.domain.statistics.sleep.daily.application.port.in.GetDailySleepStatisticsUseCase;
import whispy_server.whispy.domain.statistics.sleep.comparison.application.port.in.GetSleepPeriodComparisonUseCase;
import whispy_server.whispy.domain.statistics.sleep.summary.application.port.in.GetSleepStatisticsUseCase;
import whispy_server.whispy.domain.statistics.sleep.types.SleepPeriodType;
import whispy_server.whispy.global.document.api.statistics.SleepStatisticsApiDocument;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics/sleep")
public class SleepStatisticsController implements SleepStatisticsApiDocument {

    private final GetSleepStatisticsUseCase getSleepStatisticsUseCase;
    private final GetSleepPeriodComparisonUseCase getSleepPeriodComparisonUseCase;
    private final GetDailySleepStatisticsUseCase getDailySleepStatisticsUseCase;

    @GetMapping
    public SleepStatisticsResponse getSleepStatistics(
            @RequestParam SleepPeriodType period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return getSleepStatisticsUseCase.execute(period, date);
    }

    @GetMapping("/comparison")
    public SleepPeriodComparisonResponse getSleepPeriodComparison(
            @RequestParam SleepPeriodType period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return getSleepPeriodComparisonUseCase.execute(period, date);
    }

    @GetMapping("/daily")
    public DailySleepStatisticsResponse getDailySleepStatistics(
            @RequestParam SleepPeriodType period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return getDailySleepStatisticsUseCase.execute(period, date);
    }
}
