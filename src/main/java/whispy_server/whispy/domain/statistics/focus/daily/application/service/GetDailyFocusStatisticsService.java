package whispy_server.whispy.domain.statistics.focus.daily.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.statistics.common.constants.TimeConstants;
import whispy_server.whispy.domain.statistics.common.validator.DateValidator;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.in.web.dto.response.DailyFocusStatisticsResponse;
import whispy_server.whispy.domain.statistics.focus.daily.application.port.in.GetDailyFocusStatisticsUseCase;
import whispy_server.whispy.domain.statistics.focus.daily.application.port.out.QueryFocusStatisticsPort;
import whispy_server.whispy.domain.statistics.focus.daily.model.DailyFocusData;
import whispy_server.whispy.domain.statistics.focus.daily.model.DailyFocusStatistics;
import whispy_server.whispy.domain.statistics.focus.daily.model.HourlyFocusData;
import whispy_server.whispy.domain.statistics.focus.daily.model.MonthlyFocusData;
import whispy_server.whispy.domain.statistics.focus.types.FocusPeriodType;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.focus.FocusSessionDto;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.application.port.out.QueryUserPort;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.exception.domain.user.UserNotFoundException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class GetDailyFocusStatisticsService implements GetDailyFocusStatisticsUseCase {

    private final QueryFocusStatisticsPort queryFocusStatisticsPort;
    private final UserFacadeUseCase userFacadeUseCase;
    private final QueryUserPort queryUserPort;

    @Override
    @Transactional(readOnly = true)
    public DailyFocusStatisticsResponse execute(FocusPeriodType period, LocalDate date) {
        DateValidator.validateNotFutureDate(date);
        
        String email = userFacadeUseCase.currentUser().email();
        User user = queryUserPort.findByEmail(email)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        LocalDateTime[] range = calculatePeriodRange(period, date);
        LocalDateTime start = range[0];
        LocalDateTime end = range[1];

        List<FocusSessionDto> sessions = queryFocusStatisticsPort.findByUserIdAndPeriod(user.id(), start, end);

        DailyFocusStatistics statistics = switch (period) {
            case TODAY -> createHourlyStatistics(sessions);
            case WEEK, MONTH -> createDailyStatistics(sessions, start.toLocalDate(), end.toLocalDate());
            case YEAR -> createMonthlyStatistics(sessions, date.getYear());
        };

        return DailyFocusStatisticsResponse.from(statistics);
    }

    private DailyFocusStatistics createHourlyStatistics(List<FocusSessionDto> sessions) {
        Map<Integer, Integer> hourlyMinutesMap = sessions.stream()
                .collect(Collectors.groupingBy(
                        s -> s.startedAt().getHour(),
                        Collectors.summingInt(s -> s.durationSeconds() / TimeConstants.SECONDS_PER_MINUTE)
                ));

        List<HourlyFocusData> hourlyDataList = IntStream.range(
                        TimeConstants.FIRST_HOUR_OF_DAY,
                        TimeConstants.HOURS_PER_DAY
                )
                .mapToObj(hour -> new HourlyFocusData(
                        hour,
                        hourlyMinutesMap.getOrDefault(hour, 0)
                ))
                .toList();

        return DailyFocusStatistics.ofHourly(hourlyDataList);
    }

    private DailyFocusStatistics createDailyStatistics(
            List<FocusSessionDto> sessions,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Map<LocalDate, Integer> dailyMinutesMap = sessions.stream()
                .collect(Collectors.groupingBy(
                        s -> s.startedAt().toLocalDate(),
                        Collectors.summingInt(s -> s.durationSeconds() / TimeConstants.SECONDS_PER_MINUTE)
                ));

        List<DailyFocusData> dailyDataList = startDate.datesUntil(endDate.plusDays(1))
                .map(date -> new DailyFocusData(
                        date,
                        date.getDayOfWeek(),
                        date.getDayOfMonth(),
                        dailyMinutesMap.getOrDefault(date, 0)
                ))
                .toList();

        return DailyFocusStatistics.ofDaily(dailyDataList);
    }

    private DailyFocusStatistics createMonthlyStatistics(List<FocusSessionDto> sessions, int year) {
        Map<Integer, Integer> monthlyMinutesMap = sessions.stream()
                .collect(Collectors.groupingBy(
                        s -> s.startedAt().getMonthValue(),
                        Collectors.summingInt(s -> s.durationSeconds() / TimeConstants.SECONDS_PER_MINUTE)
                ));

        List<MonthlyFocusData> monthlyDataList = IntStream.rangeClosed(
                        TimeConstants.FIRST_MONTH_OF_YEAR,
                        TimeConstants.MONTHS_PER_YEAR
                )
                .mapToObj(month -> new MonthlyFocusData(
                        month,
                        java.time.Month.of(month),
                        monthlyMinutesMap.getOrDefault(month, 0)
                ))
                .toList();

        return DailyFocusStatistics.ofMonthly(monthlyDataList);
    }

    private LocalDateTime[] calculatePeriodRange(FocusPeriodType period, LocalDate date) {
        return switch (period) {
            case TODAY -> new LocalDateTime[]{
                    date.atStartOfDay(),
                    date.atTime(TimeConstants.END_OF_DAY)
            };
            case WEEK -> new LocalDateTime[]{
                    date.with(DayOfWeek.MONDAY).atStartOfDay(),
                    date.with(DayOfWeek.SUNDAY).atTime(TimeConstants.END_OF_DAY)
            };
            case MONTH -> new LocalDateTime[]{
                    date.withDayOfMonth(1).atStartOfDay(),
                    date.withDayOfMonth(date.lengthOfMonth()).atTime(TimeConstants.END_OF_DAY)
            };
            case YEAR -> new LocalDateTime[]{
                    date.withDayOfYear(1).atStartOfDay(),
                    date.withDayOfYear(date.lengthOfYear()).atTime(TimeConstants.END_OF_DAY)
            };
        };
    }
}
