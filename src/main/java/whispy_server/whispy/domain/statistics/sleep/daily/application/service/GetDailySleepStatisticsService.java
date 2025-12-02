package whispy_server.whispy.domain.statistics.sleep.daily.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.statistics.common.constants.TimeConstants;
import whispy_server.whispy.domain.statistics.common.util.StatisticsPeriodRangeCalculator;
import whispy_server.whispy.domain.statistics.common.validator.DateValidator;
import whispy_server.whispy.domain.statistics.sleep.daily.adapter.in.web.dto.response.DailySleepStatisticsResponse;
import whispy_server.whispy.domain.statistics.sleep.daily.adapter.out.dto.DailySleepAggregationDto;
import whispy_server.whispy.domain.statistics.sleep.daily.adapter.out.dto.MonthlySleepAggregationDto;
import whispy_server.whispy.domain.statistics.sleep.daily.application.port.in.GetDailySleepStatisticsUseCase;
import whispy_server.whispy.domain.statistics.sleep.daily.application.port.out.QuerySleepStatisticsPort;
import whispy_server.whispy.domain.statistics.sleep.daily.model.DailySleepData;
import whispy_server.whispy.domain.statistics.sleep.daily.model.DailySleepStatistics;
import whispy_server.whispy.domain.statistics.sleep.daily.model.MonthlySleepData;
import whispy_server.whispy.domain.statistics.sleep.types.SleepPeriodType;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 수면 데이터를 일/월 단위로 집계해 그래프 응답을 생성하는 서비스.
 */
@Service
@RequiredArgsConstructor
public class GetDailySleepStatisticsService implements GetDailySleepStatisticsUseCase {

    private final QuerySleepStatisticsPort querySleepStatisticsPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    @Transactional(readOnly = true)
    public DailySleepStatisticsResponse execute(SleepPeriodType period, LocalDate date) {
        DateValidator.validateNotFutureDate(date);

        User user = userFacadeUseCase.currentUser();

        LocalDateTime[] range = calculatePeriodRange(period, date);
        LocalDateTime start = range[0];
        LocalDateTime end = range[1];

        DailySleepStatistics statistics = switch (period) {
            case WEEK, MONTH -> createDailyStatistics(user.id(), start, end);
            case YEAR -> createMonthlyStatistics(user.id(), date.getYear());
        };

        return DailySleepStatisticsResponse.from(statistics);
    }

    private DailySleepStatistics createDailyStatistics(Long userId, LocalDateTime start, LocalDateTime end) {
        List<DailySleepAggregationDto> aggregations = querySleepStatisticsPort.aggregateDailyMinutes(
                userId, 
                start, 
                end
        );

        Map<LocalDate, Integer> dailyMinutesMap = aggregations.stream()
                .collect(Collectors.toMap(
                        DailySleepAggregationDto::date,
                        DailySleepAggregationDto::totalMinutes
                ));

        List<DailySleepData> dailyDataList = start.toLocalDate().datesUntil(end.toLocalDate().plusDays(1))
                .map(date -> new DailySleepData(
                        date,
                        date.getDayOfWeek(),
                        date.getDayOfMonth(),
                        dailyMinutesMap.getOrDefault(date, 0)
                ))
                .toList();

        return DailySleepStatistics.ofDaily(dailyDataList);
    }

    private DailySleepStatistics createMonthlyStatistics(Long userId, int year) {
        List<MonthlySleepAggregationDto> aggregations = querySleepStatisticsPort.aggregateMonthlyMinutes(userId, year);

        Map<Integer, Integer> monthlyMinutesMap = aggregations.stream()
                .collect(Collectors.toMap(
                        MonthlySleepAggregationDto::month,
                        MonthlySleepAggregationDto::totalMinutes
                ));

        List<MonthlySleepData> monthlyDataList = IntStream.rangeClosed(
                        TimeConstants.FIRST_MONTH_OF_YEAR,
                        TimeConstants.MONTHS_PER_YEAR
                )
                .mapToObj(month -> new MonthlySleepData(
                        month,
                        Month.of(month),
                        monthlyMinutesMap.getOrDefault(month, 0)
                ))
                .toList();

        return DailySleepStatistics.ofMonthly(monthlyDataList);
    }

    private LocalDateTime[] calculatePeriodRange(SleepPeriodType period, LocalDate date) {
        return StatisticsPeriodRangeCalculator.calculateSleepPeriodRange(period, date);
    }
}
