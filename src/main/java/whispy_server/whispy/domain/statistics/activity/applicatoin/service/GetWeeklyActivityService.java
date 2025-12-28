package whispy_server.whispy.domain.statistics.activity.applicatoin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.statistics.activity.adapter.in.web.dto.response.WeeklyActivityResponse;
import whispy_server.whispy.domain.statistics.activity.applicatoin.port.in.GetWeeklyActivityUseCase;
import whispy_server.whispy.domain.statistics.activity.applicatoin.port.out.QueryActivityMinutesPort;
import whispy_server.whispy.domain.statistics.activity.model.DayActivityData;
import whispy_server.whispy.domain.statistics.activity.model.MonthIndicator;
import whispy_server.whispy.domain.statistics.activity.model.WeekActivityData;
import whispy_server.whispy.domain.statistics.common.constants.TimeConstants;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.global.annotation.UserAction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 주간 활동 통계 조회 서비스.
 *
 * 사용자의 지난 20주간 활동 통계를 집계하여 조회하는 애플리케이션 서비스입니다.
 */
@Service
@RequiredArgsConstructor
    public class GetWeeklyActivityService implements GetWeeklyActivityUseCase {

    private static final int TOTAL_WEEKS = 20;
    private static final int DAYS_PER_WEEK = 7;

    private final QueryActivityMinutesPort queryActivityMinutesPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 주간 활동 통계를 집계하여 조회합니다.
     *
     * @return 주간 활동 통계 응답
     */
    @UserAction("주간 활동 통계 조회")
    @Override
    @Transactional(readOnly = true)
    public WeeklyActivityResponse execute() {
        Long userId = userFacadeUseCase.currentUser().id();
        LocalDate today = LocalDate.now();

        LocalDate startDate = today.minusDays((TOTAL_WEEKS * DAYS_PER_WEEK) - 1);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = today.atTime(TimeConstants.END_OF_DAY);

        Map<LocalDate, Integer> sessionMinutes = queryActivityMinutesPort.findSessionMinutesInPeriod(
                userId,
                startDateTime,
                endDateTime
        );

        List<WeekActivityData> weeks = createWeeksData(startDate, today, sessionMinutes);
        List<MonthIndicator> months = createMonthIndicators(startDate, today);

        return new WeeklyActivityResponse(startDate, today, months, weeks);
    }

    private List<WeekActivityData> createWeeksData(
            LocalDate startDate,
            LocalDate endDate,
            Map<LocalDate, Integer> sessionMinutes
    ) {
        long totalDays = startDate.datesUntil(endDate.plusDays(1)).count();
        AtomicInteger weekIndex = new AtomicInteger(0);

        return IntStream.range(0, (int) Math.ceil((double) totalDays / DAYS_PER_WEEK))
                .mapToObj(weekNum -> {
                    LocalDate weekStart = startDate.plusWeeks(weekNum);

                    List<DayActivityData> days = Stream.iterate(weekStart, date -> date.plusDays(1))
                            .limit(DAYS_PER_WEEK)
                            .takeWhile(date -> !date.isAfter(endDate))
                            .map(date -> new DayActivityData(
                                    date,
                                    date.getDayOfWeek(),
                                    sessionMinutes.getOrDefault(date, 0)
                            ))
                            .toList();

                    return new WeekActivityData(weekIndex.getAndIncrement(), days);
                })
                .filter(week -> !week.days().isEmpty())
                .toList();
    }

    private List<MonthIndicator> createMonthIndicators(LocalDate startDate, LocalDate endDate) {
        Map<YearMonth, Integer> monthStartWeeks = new LinkedHashMap<>();

        startDate.datesUntil(endDate.plusDays(1))
                .forEach(date -> {
                    YearMonth yearMonth = YearMonth.from(date);
                    int weekIndex = (int) ((date.toEpochDay() - startDate.toEpochDay()) / DAYS_PER_WEEK);
                    monthStartWeeks.putIfAbsent(yearMonth, weekIndex);
                });

        return monthStartWeeks.entrySet().stream()
                .map(entry -> new MonthIndicator(
                        entry.getKey().getYear(),
                        entry.getKey().getMonthValue(),
                        entry.getKey().getMonth(),
                        entry.getValue()
                ))
                .toList();
    }
}
