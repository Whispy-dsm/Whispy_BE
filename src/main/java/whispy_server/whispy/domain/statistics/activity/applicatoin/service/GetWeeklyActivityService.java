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

    /**
     * 주간 활동 데이터를 생성합니다.
     *
     * 시작 날짜부터 종료 날짜까지를 7일씩 묶어서 주 단위로 그룹화하고,
     * 각 날짜의 세션 시간을 매핑합니다. 세션 기록이 없는 날은 0분으로 설정됩니다.
     *
     * 20주 데이터를 생성하며, 각 주는 0부터 시작하는 weekIndex를 가집니다.
     * 마지막 주가 7일 미만일 수 있으며, 이 경우 실제 날짜 수만큼만 포함됩니다.
     *
     * 예시:
     * - Week 0: 2024-01-01(월)~2024-01-07(일) [120분, 0분, 90분, ...]
     * - Week 1: 2024-01-08(월)~2024-01-14(일) [60분, 150분, ...]
     *
     * @param startDate      시작 날짜
     * @param endDate        종료 날짜
     * @param sessionMinutes 날짜별 세션 시간 맵
     * @return 주간 활동 데이터 리스트 (빈 날짜는 0분)
     */
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

    /**
     * 월 표시 정보를 생성합니다.
     *
     * 20주 그래프에서 각 월이 시작되는 주의 인덱스를 계산합니다.
     * 클라이언트는 이 정보를 사용하여 그래프 상단에 월 레이블을 표시할 수 있습니다.
     *
     * 각 날짜를 순회하며 새로운 월이 시작되는 첫 날의 weekIndex를 기록합니다.
     * 이미 기록된 월은 건너뛰어 월의 첫 등장 위치만 저장합니다.
     *
     * 예시:
     * - 2024년 1월: weekIndex=0 (Week 0에서 시작)
     * - 2024년 2월: weekIndex=4 (Week 4에서 시작)
     * - 2024년 3월: weekIndex=9 (Week 9에서 시작)
     *
     * @param startDate 시작 날짜
     * @param endDate   종료 날짜
     * @return 월 표시 정보 리스트 (연도, 월, 시작 weekIndex 포함)
     */
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
