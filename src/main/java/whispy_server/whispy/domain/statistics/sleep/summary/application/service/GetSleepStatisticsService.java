package whispy_server.whispy.domain.statistics.sleep.summary.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.statistics.common.constants.TimeConstants;
import whispy_server.whispy.domain.statistics.common.validator.DateValidator;
import whispy_server.whispy.domain.statistics.sleep.summary.adapter.in.web.dto.response.SleepStatisticsResponse;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.sleep.SleepAggregationDto;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.sleep.SleepSessionDto;
import whispy_server.whispy.domain.statistics.sleep.summary.application.port.in.GetSleepStatisticsUseCase;
import whispy_server.whispy.domain.statistics.sleep.summary.application.port.out.QuerySleepStatisticsPort;
import whispy_server.whispy.domain.statistics.sleep.summary.model.SleepStatistics;
import whispy_server.whispy.domain.statistics.sleep.types.SleepPeriodType;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.application.port.out.QueryUserPort;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.exception.domain.user.UserNotFoundException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetSleepStatisticsService implements GetSleepStatisticsUseCase {

    private static final double MAX_CONSISTENCY_SCORE = 100.0;
    private static final double MIN_CONSISTENCY_SCORE = 0.0;
    private static final int MIN_SESSIONS_FOR_CONSISTENCY = 2;
    private static final double VARIANCE_TO_SCORE_DIVISOR = 120.0;
    private static final double VARIANCE_TO_SCORE_MULTIPLIER = 50.0;
    private static final double SCORE_ROUNDING_PRECISION = 10.0;

    private final QuerySleepStatisticsPort querySleepStatisticsPort;
    private final UserFacadeUseCase userFacadeUseCase;
    private final QueryUserPort queryUserPort;

    @Override
    @Transactional(readOnly = true)
    public SleepStatisticsResponse execute(SleepPeriodType period, LocalDate date) {
        DateValidator.validateNotFutureDate(date);
        
        String email = userFacadeUseCase.currentUser().email();
        User user = queryUserPort.findByEmail(email)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        LocalDateTime[] range = calculatePeriodRange(period, date);
        LocalDateTime start = range[0];
        LocalDateTime end = range[1];

        SleepAggregationDto aggregation = querySleepStatisticsPort.aggregateByPeriod(user.id(), start, end);
        Integer todayMinutes = querySleepStatisticsPort.sumMinutesByDate(user.id(), date);

        List<SleepSessionDto> sessions = querySleepStatisticsPort.findByUserIdAndPeriod(user.id(), start, end);

        int totalMinutes = aggregation.totalMinutes();
        int averageMinutes = calculateAverageMinutes(sessions);
        double sleepConsistency = calculateSleepConsistency(sessions);
        LocalTime averageBedTime = calculateAverageBedTime(sessions);
        LocalTime averageWakeTime = calculateAverageWakeTime(sessions);

        SleepStatistics statistics = new SleepStatistics(
                todayMinutes,
                averageMinutes,
                sleepConsistency,
                averageBedTime,
                averageWakeTime,
                totalMinutes
        );

        return SleepStatisticsResponse.from(statistics);
    }

    private LocalDateTime[] calculatePeriodRange(SleepPeriodType period, LocalDate date) {
        return switch (period) {
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
            default -> new LocalDateTime[]{
                    date.atStartOfDay(),
                    date.atTime(TimeConstants.END_OF_DAY)
            };
        };
    }

    private int calculateAverageMinutes(List<SleepSessionDto> sessions) {
        if (sessions.isEmpty()) {
            return 0;
        }
        int totalMinutes = sessions.stream()
                .mapToInt(s -> s.durationSeconds() / TimeConstants.SECONDS_PER_MINUTE)
                .sum();
        return totalMinutes / sessions.size();
    }

    private double calculateSleepConsistency(List<SleepSessionDto> sessions) {
        if (sessions.size() < MIN_SESSIONS_FOR_CONSISTENCY) {
            return MAX_CONSISTENCY_SCORE;
        }

        List<LocalTime> bedTimes = sessions.stream()
                .map(s -> s.startedAt().toLocalTime())
                .toList();

        double variance = calculateTimeVariance(bedTimes);

        double score = Math.max(
                MIN_CONSISTENCY_SCORE,
                MAX_CONSISTENCY_SCORE - (variance / VARIANCE_TO_SCORE_DIVISOR * VARIANCE_TO_SCORE_MULTIPLIER)
        );
        
        return Math.round(score * SCORE_ROUNDING_PRECISION) / SCORE_ROUNDING_PRECISION;
    }

    private double calculateTimeVariance(List<LocalTime> times) {
        if (times.isEmpty()) {
            return 0.0;
        }

        List<Integer> minutesFromMidnight = times.stream()
                .map(time -> time.getHour() * TimeConstants.MINUTES_PER_HOUR + time.getMinute())
                .toList();

        double mean = minutesFromMidnight.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        double variance = minutesFromMidnight.stream()
                .mapToDouble(minutes -> Math.pow(minutes - mean, 2))
                .average()
                .orElse(0.0);

        return Math.sqrt(variance);
    }

    private LocalTime calculateAverageBedTime(List<SleepSessionDto> sessions) {
        if (sessions.isEmpty()) {
            return LocalTime.of(0, 0);
        }

        double averageMinutes = sessions.stream()
                .mapToInt(s -> s.startedAt().getHour() * TimeConstants.MINUTES_PER_HOUR 
                        + s.startedAt().getMinute())
                .average()
                .orElse(0.0);

        int hours = (int) (averageMinutes / TimeConstants.MINUTES_PER_HOUR);
        int minutes = (int) (averageMinutes % TimeConstants.MINUTES_PER_HOUR);

        return LocalTime.of(hours, minutes);
    }

    private LocalTime calculateAverageWakeTime(List<SleepSessionDto> sessions) {
        if (sessions.isEmpty()) {
            return LocalTime.of(0, 0);
        }

        double averageMinutes = sessions.stream()
                .mapToInt(s -> s.endedAt().getHour() * TimeConstants.MINUTES_PER_HOUR 
                        + s.endedAt().getMinute())
                .average()
                .orElse(0.0);

        int hours = (int) (averageMinutes / TimeConstants.MINUTES_PER_HOUR);
        int minutes = (int) (averageMinutes % TimeConstants.MINUTES_PER_HOUR);

        return LocalTime.of(hours, minutes);
    }
}
