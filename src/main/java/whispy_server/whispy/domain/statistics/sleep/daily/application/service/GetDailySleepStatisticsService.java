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
import whispy_server.whispy.global.annotation.UserAction;

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
    @UserAction("일별 수면 통계 조회")
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

    /**
     * 일별 수면 통계를 생성합니다. (주간/월간 조회용)
     *
     * DB에서 실제 기록이 있는 날짜의 수면 시간만 조회한 후,
     * 기간 내 모든 날짜를 순회하며 기록이 없는 날은 0분으로 채워 연속된 그래프 데이터를 생성합니다.
     *
     * 예시:
     * - 조회 기간: 2024-01-01 ~ 2024-01-07
     * - DB 집계: [{2024-01-03: 420}, {2024-01-05: 480}]
     * - 결과: 1일(0), 2일(0), 3일(420), 4일(0), 5일(480), 6일(0), 7일(0)
     *
     * @param userId 사용자 ID
     * @param start  시작 일시
     * @param end    종료 일시
     * @return 일별 수면 통계 (빈 날짜 포함)
     */
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

    /**
     * 월별 수면 통계를 생성합니다. (연간 조회용)
     *
     * DB에서 실제 기록이 있는 월의 수면 시간만 조회한 후,
     * 1월부터 12월까지 모든 월을 순회하며 기록이 없는 월은 0분으로 채워 연간 그래프 데이터를 생성합니다.
     *
     * 예시:
     * - 조회 연도: 2024년
     * - DB 집계: [{3월: 12600}, {4월: 13200}]
     * - 결과: 1월(0), 2월(0), 3월(12600), 4월(13200), ..., 12월(0)
     *
     * @param userId 사용자 ID
     * @param year   조회 연도
     * @return 월별 수면 통계 (빈 월 포함)
     */
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

    /**
     * 수면 통계 조회 기간을 계산합니다.
     *
     * 기간 타입(WEEK, MONTH, YEAR)에 따라 시작일시와 종료일시를 계산합니다.
     *
     * @param period 통계 기간 타입 (주간/월간/연간)
     * @param date   기준 날짜
     * @return [시작일시, 종료일시] 배열
     */
    private LocalDateTime[] calculatePeriodRange(SleepPeriodType period, LocalDate date) {
        return StatisticsPeriodRangeCalculator.calculateSleepPeriodRange(period, date);
    }
}
