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

/**
 * 수면 통계 REST 컨트롤러.
 *
 * 수면 통계, 기간 비교, 일일 상세 통계를 조회하는 인바운드 어댑터입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics/sleep")
public class SleepStatisticsController implements SleepStatisticsApiDocument {

    private final GetSleepStatisticsUseCase getSleepStatisticsUseCase;
    private final GetSleepPeriodComparisonUseCase getSleepPeriodComparisonUseCase;
    private final GetDailySleepStatisticsUseCase getDailySleepStatisticsUseCase;

    /**
     * 수면 통계를 조회합니다.
     *
     * @param period 통계 기간 타입
     * @param date 기준 날짜
     * @return 수면 통계 응답
     */
    @GetMapping
    public SleepStatisticsResponse getSleepStatistics(
            @RequestParam SleepPeriodType period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return getSleepStatisticsUseCase.execute(period, date);
    }

    /**
     * 수면 기간 비교 통계를 조회합니다.
     *
     * @param period 통계 기간 타입
     * @param date 기준 날짜
     * @return 수면 기간 비교 응답
     */
    @GetMapping("/comparison")
    public SleepPeriodComparisonResponse getSleepPeriodComparison(
            @RequestParam SleepPeriodType period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return getSleepPeriodComparisonUseCase.execute(period, date);
    }

    /**
     * 일일 수면 통계를 조회합니다.
     *
     * @param period 통계 기간 타입
     * @param date 기준 날짜
     * @return 일일 수면 통계 응답
     */
    @GetMapping("/daily")
    public DailySleepStatisticsResponse getDailySleepStatistics(
            @RequestParam SleepPeriodType period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return getDailySleepStatisticsUseCase.execute(period, date);
    }
}
