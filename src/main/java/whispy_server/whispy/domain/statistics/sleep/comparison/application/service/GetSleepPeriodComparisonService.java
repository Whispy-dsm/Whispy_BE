package whispy_server.whispy.domain.statistics.sleep.comparison.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.statistics.common.constants.TimeConstants;
import whispy_server.whispy.domain.statistics.common.validator.DateValidator;
import whispy_server.whispy.domain.statistics.sleep.comparison.adapter.in.web.dto.response.SleepPeriodComparisonResponse;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.sleep.SleepSessionDto;
import whispy_server.whispy.domain.statistics.sleep.comparison.application.port.in.GetSleepPeriodComparisonUseCase;
import whispy_server.whispy.domain.statistics.sleep.comparison.application.port.out.QuerySleepComparisonPort;
import whispy_server.whispy.domain.statistics.sleep.comparison.model.SleepPeriodComparison;
import whispy_server.whispy.domain.statistics.sleep.types.SleepPeriodType;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.annotation.UserAction;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 수면 기간 비교 조회 서비스.
 *
 * 사용자의 수면 통계를 기간별로 비교하여 조회하는 애플리케이션 서비스입니다.
 */
@Service
@RequiredArgsConstructor
public class GetSleepPeriodComparisonService implements GetSleepPeriodComparisonUseCase {

    private static final int CURRENT_PERIOD_OFFSET = 0;
    private static final int PREVIOUS_PERIOD_OFFSET = 1;
    private static final int TWO_PERIODS_AGO_OFFSET = 2;

    private final QuerySleepComparisonPort querySleepComparisonPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 수면 기간 비교 통계를 조회합니다.
     *
     * @param period 통계 기간 타입
     * @param date 기준 날짜
     * @return 수면 기간 비교 응답
     */
    @Override
    @Transactional(readOnly = true)
    @UserAction("수면 기간 비교 조회")
    public SleepPeriodComparisonResponse execute(SleepPeriodType period, LocalDate date) {
        DateValidator.validateNotFutureDate(date);

        User user = userFacadeUseCase.currentUser();

        int currentMinutes = calculatePeriodMinutes(user.id(), period, date, CURRENT_PERIOD_OFFSET);
        int previousMinutes = calculatePeriodMinutes(user.id(), period, date, PREVIOUS_PERIOD_OFFSET);
        int twoPeriodAgoMinutes = calculatePeriodMinutes(user.id(), period, date, TWO_PERIODS_AGO_OFFSET);

        int difference = currentMinutes - previousMinutes;

        SleepPeriodComparison comparison = new SleepPeriodComparison(
                currentMinutes,
                previousMinutes,
                twoPeriodAgoMinutes,
                difference
        );

        return SleepPeriodComparisonResponse.from(comparison);
    }

    private int calculatePeriodMinutes(Long userId, SleepPeriodType period, LocalDate baseDate, int periodsAgo) {
        LocalDateTime[] range = calculatePeriodRange(period, baseDate, periodsAgo);
        List<SleepSessionDto> sessions = querySleepComparisonPort.findByUserIdAndPeriod(
                userId,
                range[0],
                range[1]
        );

        return sessions.stream()
                .mapToInt(s -> s.durationSeconds() / TimeConstants.SECONDS_PER_MINUTE)
                .sum();
    }

    private LocalDateTime[] calculatePeriodRange(SleepPeriodType period, LocalDate date, int periodsAgo) {
        LocalDate targetDate = switch (period) {
            case WEEK -> date.minusWeeks(periodsAgo);
            case MONTH -> date.minusMonths(periodsAgo);
            case YEAR -> date.minusYears(periodsAgo);
            default -> date;
        };

        return switch (period) {
            case WEEK -> new LocalDateTime[]{
                    targetDate.with(DayOfWeek.MONDAY).atStartOfDay(),
                    targetDate.with(DayOfWeek.SUNDAY).atTime(TimeConstants.END_OF_DAY)
            };
            case MONTH -> new LocalDateTime[]{
                    targetDate.withDayOfMonth(1).atStartOfDay(),
                    targetDate.withDayOfMonth(targetDate.lengthOfMonth()).atTime(TimeConstants.END_OF_DAY)
            };
            case YEAR -> new LocalDateTime[]{
                    targetDate.withDayOfYear(1).atStartOfDay(),
                    targetDate.withDayOfYear(targetDate.lengthOfYear()).atTime(TimeConstants.END_OF_DAY)
            };
            default -> new LocalDateTime[]{
                    targetDate.atStartOfDay(),
                    targetDate.atTime(TimeConstants.END_OF_DAY)
            };
        };
    }
}
