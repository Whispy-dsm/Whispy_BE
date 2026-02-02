package whispy_server.whispy.domain.statistics.focus.daily.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.statistics.common.constants.TimeConstants;
import whispy_server.whispy.domain.statistics.common.util.StatisticsPeriodRangeCalculator;
import whispy_server.whispy.domain.statistics.common.validator.DateValidator;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.in.web.dto.response.DailyFocusStatisticsResponse;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.DailyFocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.DailyTagFocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.HourlyFocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.HourlyTagFocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.MonthlyFocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.MonthlyTagFocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.daily.application.port.in.GetDailyFocusStatisticsUseCase;
import whispy_server.whispy.domain.statistics.focus.daily.application.port.out.QueryFocusStatisticsPort;
import whispy_server.whispy.domain.statistics.focus.daily.model.DailyFocusData;
import whispy_server.whispy.domain.statistics.focus.daily.model.DailyFocusStatistics;
import whispy_server.whispy.domain.statistics.focus.daily.model.HourlyFocusData;
import whispy_server.whispy.domain.statistics.focus.daily.model.MonthlyFocusData;
import whispy_server.whispy.domain.statistics.focus.daily.model.TagFocusData;
import whispy_server.whispy.domain.statistics.focus.types.FocusPeriodType;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.annotation.UserAction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 집중 세션을 기준에 맞춰 시간별/일별/월별로 집계하는 서비스.
 */
@Service
@RequiredArgsConstructor
public class GetDailyFocusStatisticsService implements GetDailyFocusStatisticsUseCase {

    private final QueryFocusStatisticsPort queryFocusStatisticsPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    @Transactional(readOnly = true)
    @UserAction("일별 집중 통계 조회")
    public DailyFocusStatisticsResponse execute(FocusPeriodType period, LocalDate date) {
        DateValidator.validateNotFutureDate(date);

        User user = userFacadeUseCase.currentUser();

        LocalDateTime[] range = calculatePeriodRange(period, date);
        LocalDateTime start = range[0];
        LocalDateTime end = range[1];

        DailyFocusStatistics statistics = switch (period) {
            case TODAY -> createHourlyStatistics(user.id(), start, end);
            case WEEK, MONTH -> createDailyStatistics(user.id(), start, end);
            case YEAR -> createMonthlyStatistics(user.id(), date.getYear());
        };

        return DailyFocusStatisticsResponse.from(statistics);
    }

    /**
     * 시간별 집중 통계를 생성합니다. (당일 조회용)
     *
     * 당일 00:00~23:59의 집중 세션을 시간대별로 집계하며,
     * 각 시간대마다 태그별 집중 시간도 함께 제공합니다.
     * 기록이 없는 시간대는 0분으로 채워 24시간 연속 데이터를 생성합니다.
     *
     * 예시:
     * - 9시: 총 60분 (WORK: 40분, STUDY: 20분)
     * - 10시: 총 0분 (모든 태그 0분)
     * - 14시: 총 90분 (STUDY: 90분)
     *
     * @param userId 사용자 ID
     * @param start  당일 00:00
     * @param end    당일 23:59
     * @return 시간별 집중 통계 (0~23시, 빈 시간 포함)
     */
    private DailyFocusStatistics createHourlyStatistics(Long userId, LocalDateTime start, LocalDateTime end) {
        List<HourlyFocusAggregationDto> aggregations = queryFocusStatisticsPort.aggregateHourlyMinutes(
                userId,
                start,
                end
        );
        List<HourlyTagFocusAggregationDto> tagAggregations = queryFocusStatisticsPort.aggregateHourlyByTag(
                userId,
                start,
                end
        );

        Map<Integer, Integer> hourlyMinutesMap = aggregations.stream()
                .collect(Collectors.toMap(
                        HourlyFocusAggregationDto::hour,
                        HourlyFocusAggregationDto::totalMinutes
                ));

        Map<Integer, List<HourlyTagFocusAggregationDto>> hourlyTagMap = tagAggregations.stream()
                .collect(Collectors.groupingBy(HourlyTagFocusAggregationDto::hour));

        List<HourlyFocusData> hourlyDataList = IntStream.range(
                        TimeConstants.FIRST_HOUR_OF_DAY,
                        TimeConstants.HOURS_PER_DAY
                )
                .mapToObj(hour -> {
                    List<TagFocusData> tagDataList = convertTagData(
                            hourlyTagMap.getOrDefault(hour, List.of()),
                            HourlyTagFocusAggregationDto::tag,
                            HourlyTagFocusAggregationDto::minutes
                    );
                    return new HourlyFocusData(
                            hour,
                            hourlyMinutesMap.getOrDefault(hour, 0),
                            tagDataList
                    );
                })
                .toList();

        return DailyFocusStatistics.ofHourly(hourlyDataList);
    }

    /**
     * 일별 집중 통계를 생성합니다. (주간/월간 조회용)
     *
     * 기간 내 집중 세션을 날짜별로 집계하며,
     * 각 날짜마다 태그별 집중 시간도 함께 제공합니다.
     * 기록이 없는 날짜는 0분으로 채워 연속된 그래프 데이터를 생성합니다.
     *
     * 예시:
     * - 2024-01-01: 총 120분 (WORK: 80분, STUDY: 40분)
     * - 2024-01-02: 총 0분 (모든 태그 0분)
     * - 2024-01-03: 총 180분 (STUDY: 180분)
     *
     * @param userId 사용자 ID
     * @param start  시작 일시
     * @param end    종료 일시
     * @return 일별 집중 통계 (빈 날짜 포함)
     */
    private DailyFocusStatistics createDailyStatistics(Long userId, LocalDateTime start, LocalDateTime end) {
        List<DailyFocusAggregationDto> aggregations = queryFocusStatisticsPort.aggregateDailyMinutes(
                userId,
                start,
                end
        );
        List<DailyTagFocusAggregationDto> tagAggregations = queryFocusStatisticsPort.aggregateDailyByTag(
                userId,
                start,
                end
        );

        Map<LocalDate, Integer> dailyMinutesMap = aggregations.stream()
                .collect(Collectors.toMap(
                        DailyFocusAggregationDto::date,
                        DailyFocusAggregationDto::totalMinutes
                ));

        Map<LocalDate, List<DailyTagFocusAggregationDto>> dailyTagMap = tagAggregations.stream()
                .collect(Collectors.groupingBy(DailyTagFocusAggregationDto::date));

        List<DailyFocusData> dailyDataList = start.toLocalDate().datesUntil(end.toLocalDate().plusDays(1))
                .map(date -> {
                    List<TagFocusData> tagDataList = convertTagData(
                            dailyTagMap.getOrDefault(date, List.of()),
                            DailyTagFocusAggregationDto::tag,
                            DailyTagFocusAggregationDto::minutes
                    );
                    return new DailyFocusData(
                            date,
                            date.getDayOfWeek(),
                            date.getDayOfMonth(),
                            dailyMinutesMap.getOrDefault(date, 0),
                            tagDataList
                    );
                })
                .toList();

        return DailyFocusStatistics.ofDaily(dailyDataList);
    }

    /**
     * 월별 집중 통계를 생성합니다. (연간 조회용)
     *
     * 연간 집중 세션을 월별로 집계하며,
     * 각 월마다 태그별 집중 시간도 함께 제공합니다.
     * 기록이 없는 월은 0분으로 채워 1~12월 연속 데이터를 생성합니다.
     *
     * 예시:
     * - 2024년 1월: 총 3600분 (WORK: 2400분, STUDY: 1200분)
     * - 2024년 2월: 총 0분 (모든 태그 0분)
     * - 2024년 3월: 총 4200분 (STUDY: 4200분)
     *
     * @param userId 사용자 ID
     * @param year   조회 연도
     * @return 월별 집중 통계 (빈 월 포함)
     */
    private DailyFocusStatistics createMonthlyStatistics(Long userId, int year) {
        List<MonthlyFocusAggregationDto> aggregations = queryFocusStatisticsPort.aggregateMonthlyMinutes(userId, year);
        List<MonthlyTagFocusAggregationDto> tagAggregations = queryFocusStatisticsPort.aggregateMonthlyByTag(userId, year);

        Map<Integer, Integer> monthlyMinutesMap = aggregations.stream()
                .collect(Collectors.toMap(
                        MonthlyFocusAggregationDto::month,
                        MonthlyFocusAggregationDto::totalMinutes
                ));

        Map<Integer, List<MonthlyTagFocusAggregationDto>> monthlyTagMap = tagAggregations.stream()
                .collect(Collectors.groupingBy(MonthlyTagFocusAggregationDto::month));

        List<MonthlyFocusData> monthlyDataList = IntStream.rangeClosed(
                        TimeConstants.FIRST_MONTH_OF_YEAR,
                        TimeConstants.MONTHS_PER_YEAR
                )
                .mapToObj(month -> {
                    List<TagFocusData> tagDataList = convertTagData(
                            monthlyTagMap.getOrDefault(month, List.of()),
                            MonthlyTagFocusAggregationDto::tag,
                            MonthlyTagFocusAggregationDto::minutes
                    );
                    return new MonthlyFocusData(
                            month,
                            Month.of(month),
                            monthlyMinutesMap.getOrDefault(month, 0),
                            tagDataList
                    );
                })
                .toList();

        return DailyFocusStatistics.ofMonthly(monthlyDataList);
    }

    /**
     * 태그별 집중 시간 집계 DTO를 TagFocusData 리스트로 변환합니다.
     *
     * DB 집계 결과는 실제 기록이 있는 태그만 포함하므로,
     * 모든 FocusTag enum 값을 순회하며 기록이 없는 태그는 0분으로 채웁니다.
     * 제네릭 메서드로 구현하여 시간별/일별/월별 집계에서 재사용합니다.
     *
     * 예시:
     * - DB 집계: [{WORK: 60}, {STUDY: 30}]
     * - 결과: [{WORK: 60}, {STUDY: 30}, {READING: 0}, {EXERCISE: 0}, ...]
     *
     * @param aggregations     DB 집계 결과 리스트
     * @param tagExtractor     태그 추출 함수
     * @param minutesExtractor 시간 추출 함수
     * @param <T>              집계 DTO 타입 (HourlyTagFocusAggregationDto, DailyTagFocusAggregationDto 등)
     * @return 모든 FocusTag를 포함하는 TagFocusData 리스트
     */
    private <T> List<TagFocusData> convertTagData(
            List<T> aggregations,
            Function<T, FocusTag> tagExtractor,
            Function<T, Integer> minutesExtractor
    ) {
        Map<FocusTag, Integer> tagMinutesMap = aggregations.stream()
                .collect(Collectors.toMap(tagExtractor, minutesExtractor));

        return Arrays.stream(FocusTag.values())
                .map(tag -> new TagFocusData(tag, tagMinutesMap.getOrDefault(tag, 0)))
                .toList();
    }

    /**
     * 집중 통계 조회 기간을 계산합니다.
     *
     * 기간 타입(TODAY, WEEK, MONTH, YEAR)에 따라 시작일시와 종료일시를 계산합니다.
     *
     * @param period 통계 기간 타입 (당일/주간/월간/연간)
     * @param date   기준 날짜
     * @return [시작일시, 종료일시] 배열
     */
    private LocalDateTime[] calculatePeriodRange(FocusPeriodType period, LocalDate date) {
        return StatisticsPeriodRangeCalculator.calculateFocusPeriodRange(period, date);
    }
}
