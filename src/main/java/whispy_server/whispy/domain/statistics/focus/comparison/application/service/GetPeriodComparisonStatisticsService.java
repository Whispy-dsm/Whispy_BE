package whispy_server.whispy.domain.statistics.focus.comparison.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.statistics.common.constants.TimeConstants;
import whispy_server.whispy.domain.statistics.common.util.StatisticsPeriodRangeCalculator;
import whispy_server.whispy.domain.statistics.common.validator.DateValidator;
import whispy_server.whispy.domain.statistics.focus.comparison.adapter.in.web.dto.response.PeriodComparisonResponse;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.focus.FocusSessionDto;
import whispy_server.whispy.domain.statistics.focus.comparison.application.port.in.GetPeriodComparisonStatisticsUseCase;
import whispy_server.whispy.domain.statistics.focus.comparison.application.port.out.QueryFocusComparisonPort;
import whispy_server.whispy.domain.statistics.focus.comparison.model.PeriodComparisonStatistics;
import whispy_server.whispy.domain.statistics.focus.types.FocusPeriodType;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.annotation.UserAction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 현재/이전/이전이전 기간의 집중 시간을 비교 지표로 계산하는 서비스.
 */
@Service
@RequiredArgsConstructor
public class GetPeriodComparisonStatisticsService implements GetPeriodComparisonStatisticsUseCase {

    private static final int CURRENT_PERIOD_OFFSET = 0;
    private static final int PREVIOUS_PERIOD_OFFSET = 1;
    private static final int TWO_PERIODS_AGO_OFFSET = 2;

    private final QueryFocusComparisonPort queryFocusComparisonPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @UserAction("집중 기간 비교 통계 조회")
    @Override
    @Transactional(readOnly = true)
    public PeriodComparisonResponse execute(FocusPeriodType period, LocalDate date) {
        DateValidator.validateNotFutureDate(date);

        User user = userFacadeUseCase.currentUser();

        int currentMinutes = calculatePeriodMinutes(user.id(), period, date, CURRENT_PERIOD_OFFSET);
        int previousMinutes = calculatePeriodMinutes(user.id(), period, date, PREVIOUS_PERIOD_OFFSET);
        int twoPeriodAgoMinutes = calculatePeriodMinutes(user.id(), period, date, TWO_PERIODS_AGO_OFFSET);

        int difference = currentMinutes - previousMinutes;

        PeriodComparisonStatistics statistics = new PeriodComparisonStatistics(
                currentMinutes,
                previousMinutes,
                twoPeriodAgoMinutes,
                difference
        );

        return PeriodComparisonResponse.from(statistics);
    }

    /**
     * 특정 기간의 총 집중 시간을 계산합니다.
     *
     * 기준 날짜로부터 N개 기간 이전의 집중 세션을 조회하여 총 집중 시간을 분 단위로 계산합니다.
     *
     * 예시:
     * - period=WEEK, baseDate=2024-01-15, periodsAgo=0: 2024-01-15가 속한 주의 집중 시간
     * - period=WEEK, baseDate=2024-01-15, periodsAgo=1: 그 전 주의 집중 시간
     * - period=MONTH, baseDate=2024-01-15, periodsAgo=2: 2개월 전의 집중 시간
     *
     * @param userId     사용자 ID
     * @param period     기간 타입 (주간/월간/연간)
     * @param baseDate   기준 날짜
     * @param periodsAgo 몇 기간 이전인지 (0=현재 기간, 1=이전 기간, 2=2기간 전)
     * @return 해당 기간의 총 집중 시간 (분)
     */
    private int calculatePeriodMinutes(Long userId, FocusPeriodType period, LocalDate baseDate, int periodsAgo) {
        LocalDateTime[] range = calculatePeriodRange(period, baseDate, periodsAgo);
        List<FocusSessionDto> sessions = queryFocusComparisonPort.findByUserIdAndPeriod(
                userId,
                range[0],
                range[1]
        );

        return sessions.stream()
                .mapToInt(s -> s.durationSeconds() / TimeConstants.SECONDS_PER_MINUTE)
                .sum();
    }

    /**
     * 집중 비교 기간의 시작일시와 종료일시를 계산합니다.
     *
     * 기준 날짜로부터 N개 기간 이전의 날짜를 계산한 후,
     * StatisticsPeriodRangeCalculator를 통해 해당 기간의 시작/종료 범위를 구합니다.
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
    private LocalDateTime[] calculatePeriodRange(FocusPeriodType period, LocalDate date, int periodsAgo) {
        LocalDate targetDate = switch (period) {
            case WEEK -> date.minusWeeks(periodsAgo);
            case MONTH -> date.minusMonths(periodsAgo);
            case YEAR -> date.minusYears(periodsAgo);
            default -> date;
        };

        return StatisticsPeriodRangeCalculator.calculateFocusPeriodRange(period, targetDate);
    }
}
