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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPeriodComparisonStatisticsService implements GetPeriodComparisonStatisticsUseCase {

    private static final int CURRENT_PERIOD_OFFSET = 0;
    private static final int PREVIOUS_PERIOD_OFFSET = 1;
    private static final int TWO_PERIODS_AGO_OFFSET = 2;

    private final QueryFocusComparisonPort queryFocusComparisonPort;
    private final UserFacadeUseCase userFacadeUseCase;

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
