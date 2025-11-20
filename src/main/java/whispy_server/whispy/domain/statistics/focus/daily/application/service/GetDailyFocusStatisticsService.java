package whispy_server.whispy.domain.statistics.focus.daily.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.statistics.common.constants.TimeConstants;
import whispy_server.whispy.domain.statistics.common.util.StatisticsPeriodRangeCalculator;
import whispy_server.whispy.domain.statistics.common.validator.DateValidator;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.in.web.dto.response.DailyFocusStatisticsResponse;
import whispy_server.whispy.domain.statistics.focus.daily.application.port.in.GetDailyFocusStatisticsUseCase;
import whispy_server.whispy.domain.statistics.focus.daily.application.port.out.QueryFocusStatisticsPort;
import whispy_server.whispy.domain.statistics.focus.daily.model.DailyFocusData;
import whispy_server.whispy.domain.statistics.focus.daily.model.DailyFocusStatistics;
import whispy_server.whispy.domain.statistics.focus.daily.model.HourlyFocusData;
import whispy_server.whispy.domain.statistics.focus.daily.model.MonthlyFocusData;
import whispy_server.whispy.domain.statistics.focus.daily.model.TagFocusData;
import whispy_server.whispy.domain.statistics.focus.types.FocusPeriodType;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.*;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class GetDailyFocusStatisticsService implements GetDailyFocusStatisticsUseCase {

    private final QueryFocusStatisticsPort queryFocusStatisticsPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    @Transactional(readOnly = true)
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

    private LocalDateTime[] calculatePeriodRange(FocusPeriodType period, LocalDate date) {
        return StatisticsPeriodRangeCalculator.calculateFocusPeriodRange(period, date);
    }
}
