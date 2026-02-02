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

    /**
     * 특정 기간의 총 수면 시간을 계산합니다.
     *
     * 기준 날짜로부터 N개 기간 이전의 수면 세션을 조회하여 총 수면 시간을 분 단위로 계산합니다.
     *
     * 예시:
     * - period=WEEK, baseDate=2024-01-15, periodsAgo=0: 2024-01-15가 속한 주의 수면 시간
     * - period=WEEK, baseDate=2024-01-15, periodsAgo=1: 그 전 주의 수면 시간
     * - period=MONTH, baseDate=2024-01-15, periodsAgo=2: 2개월 전의 수면 시간
     *
     * @param userId     사용자 ID
     * @param period     기간 타입 (주간/월간/연간)
     * @param baseDate   기준 날짜
     * @param periodsAgo 몇 기간 이전인지 (0=현재 기간, 1=이전 기간, 2=2기간 전)
     * @return 해당 기간의 총 수면 시간 (분)
     */
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

    /**
     * 수면 비교 기간의 시작일시와 종료일시를 계산합니다.
     *
     * 기준 날짜로부터 N개 기간 이전의 시작/종료 범위를 계산합니다.
     * 각 기간 타입에 따라 다른 계산 방식을 사용합니다:
     * - WEEK: 월요일 00:00 ~ 일요일 23:59
     * - MONTH: 1일 00:00 ~ 말일 23:59
     * - YEAR: 1월 1일 00:00 ~ 12월 31일 23:59
     *
     * 예시:
     * - WEEK, 2024-01-15 (월), periodsAgo=0: 2024-01-15 00:00 ~ 2024-01-21 23:59
     * - WEEK, 2024-01-15 (월), periodsAgo=1: 2024-01-08 00:00 ~ 2024-01-14 23:59
     * - MONTH, 2024-01-15, periodsAgo=0: 2024-01-01 00:00 ~ 2024-01-31 23:59
     *
     * @param period     기간 타입 (주간/월간/연간)
     * @param date       기준 날짜
     * @param periodsAgo 몇 기간 이전인지
     * @return [시작일시, 종료일시] 배열
     */
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
