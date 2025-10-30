package whispy_server.whispy.domain.statistics.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.statistics.adapter.in.web.dto.response.PeriodComparisonResponse;
import whispy_server.whispy.domain.statistics.adapter.out.dto.FocusSessionDto;
import whispy_server.whispy.domain.statistics.application.port.in.GetPeriodComparisonStatisticsUseCase;
import whispy_server.whispy.domain.statistics.application.port.out.QueryFocusStatisticsPort;
import whispy_server.whispy.domain.statistics.model.PeriodComparisonStatistics;
import whispy_server.whispy.domain.statistics.model.types.PeriodType;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.application.port.out.QueryUserPort;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.exception.domain.user.UserNotFoundException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPeriodComparisonStatisticsService implements GetPeriodComparisonStatisticsUseCase {

    private final QueryFocusStatisticsPort queryFocusStatisticsPort;
    private final UserFacadeUseCase userFacadeUseCase;
    private final QueryUserPort queryUserPort;

    @Override
    @Transactional(readOnly = true)
    public PeriodComparisonResponse execute(PeriodType period, LocalDate date) {
        String email = userFacadeUseCase.currentUser().email();
        User user = queryUserPort.findByEmail(email)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        int currentMinutes = calculatePeriodMinutes(user.id(), period, date, 0);
        int previousMinutes = calculatePeriodMinutes(user.id(), period, date, 1);
        int twoPeriodAgoMinutes = calculatePeriodMinutes(user.id(), period, date, 2);

        int difference = currentMinutes - previousMinutes;

        PeriodComparisonStatistics statistics = new PeriodComparisonStatistics(
                currentMinutes,
                previousMinutes,
                twoPeriodAgoMinutes,
                difference
        );

        return PeriodComparisonResponse.from(statistics);
    }

    private int calculatePeriodMinutes(Long userId, PeriodType period, LocalDate baseDate, int periodsAgo) {
        LocalDateTime[] range = calculatePeriodRange(period, baseDate, periodsAgo);
        List<FocusSessionDto> sessions = queryFocusStatisticsPort.findByUserIdAndPeriod(
                userId,
                range[0],
                range[1]
        );

        return sessions.stream()
                .mapToInt(s -> s.durationSeconds() / 60)
                .sum();
    }

    private LocalDateTime[] calculatePeriodRange(PeriodType period, LocalDate date, int periodsAgo) {
        LocalDate targetDate = switch (period) {
            case WEEK -> date.minusWeeks(periodsAgo);
            case MONTH -> date.minusMonths(periodsAgo);
            case YEAR -> date.minusYears(periodsAgo);
            default -> date;
        };

        return switch (period) {
            case WEEK -> new LocalDateTime[]{
                    targetDate.with(DayOfWeek.MONDAY).atStartOfDay(),
                    targetDate.with(DayOfWeek.SUNDAY).atTime(23, 59, 59)
            };
            case MONTH -> new LocalDateTime[]{
                    targetDate.withDayOfMonth(1).atStartOfDay(),
                    targetDate.withDayOfMonth(targetDate.lengthOfMonth()).atTime(23, 59, 59)
            };
            case YEAR -> new LocalDateTime[]{
                    targetDate.withDayOfYear(1).atStartOfDay(),
                    targetDate.withDayOfYear(targetDate.lengthOfYear()).atTime(23, 59, 59)
            };
            default -> new LocalDateTime[]{
                    targetDate.atStartOfDay(),
                    targetDate.atTime(23, 59, 59)
            };
        };
    }
}
