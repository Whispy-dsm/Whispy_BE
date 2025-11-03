package whispy_server.whispy.domain.statistics.sleep.summary.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
                    date.with(DayOfWeek.SUNDAY).atTime(23, 59, 59)
            };
            case MONTH -> new LocalDateTime[]{
                    date.withDayOfMonth(1).atStartOfDay(),
                    date.withDayOfMonth(date.lengthOfMonth()).atTime(23, 59, 59)
            };
            case YEAR -> new LocalDateTime[]{
                    date.withDayOfYear(1).atStartOfDay(),
                    date.withDayOfYear(date.lengthOfYear()).atTime(23, 59, 59)
            };
            default -> new LocalDateTime[]{
                    date.atStartOfDay(),
                    date.atTime(23, 59, 59)
            };
        };
    }

    private int calculateAverageMinutes(List<SleepSessionDto> sessions) {
        if (sessions.isEmpty()) {
            return 0;
        }
        int totalMinutes = sessions.stream()
                .mapToInt(s -> s.durationSeconds() / 60)
                .sum();
        return totalMinutes / sessions.size();
    }

    private double calculateSleepConsistency(List<SleepSessionDto> sessions) {
        if (sessions.size() < 2) {
            return 100.0;
        }

        List<LocalTime> bedTimes = sessions.stream()
                .map(s -> s.startedAt().toLocalTime())
                .toList();

        double variance = calculateTimeVariance(bedTimes);

        double score = Math.max(0, 100 - (variance / 120.0 * 50));
        return Math.round(score * 10) / 10.0;
    }

    private double calculateTimeVariance(List<LocalTime> times) {
        if (times.isEmpty()) {
            return 0.0;
        }

        List<Integer> minutesFromMidnight = times.stream()
                .map(time -> time.getHour() * 60 + time.getMinute())
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
                .mapToInt(s -> s.startedAt().getHour() * 60 + s.startedAt().getMinute())
                .average()
                .orElse(0.0);

        int hours = (int) (averageMinutes / 60);
        int minutes = (int) (averageMinutes % 60);

        return LocalTime.of(hours, minutes);
    }

    private LocalTime calculateAverageWakeTime(List<SleepSessionDto> sessions) {
        if (sessions.isEmpty()) {
            return LocalTime.of(0, 0);
        }

        double averageMinutes = sessions.stream()
                .mapToInt(s -> s.endedAt().getHour() * 60 + s.endedAt().getMinute())
                .average()
                .orElse(0.0);

        int hours = (int) (averageMinutes / 60);
        int minutes = (int) (averageMinutes % 60);

        return LocalTime.of(hours, minutes);
    }
}
