package whispy_server.whispy.domain.statistics.sleep.summary.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.statistics.common.constants.TimeConstants;
import whispy_server.whispy.domain.statistics.common.util.StatisticsPeriodRangeCalculator;
import whispy_server.whispy.domain.statistics.common.validator.DateValidator;
import whispy_server.whispy.domain.statistics.sleep.summary.adapter.in.web.dto.response.SleepStatisticsResponse;
import whispy_server.whispy.domain.statistics.sleep.summary.adapter.out.dto.SleepDetailedAggregationDto;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.sleep.SleepSessionDto;
import whispy_server.whispy.domain.statistics.sleep.summary.application.port.in.GetSleepStatisticsUseCase;
import whispy_server.whispy.domain.statistics.sleep.summary.application.port.out.QuerySleepStatisticsPort;
import whispy_server.whispy.domain.statistics.sleep.summary.model.SleepStatistics;
import whispy_server.whispy.domain.statistics.sleep.types.SleepPeriodType;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.annotation.UserAction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 사용자 수면 세션을 기간별로 집계해 통계 응답을 생성하는 서비스.
 */
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

    @Override
    @Transactional(readOnly = true)
    @UserAction("수면 통계 조회")
    public SleepStatisticsResponse execute(SleepPeriodType period, LocalDate date) {
        DateValidator.validateNotFutureDate(date);

        User user = userFacadeUseCase.currentUser();

        LocalDateTime[] range = calculatePeriodRange(period, date);
        LocalDateTime start = range[0];
        LocalDateTime end = range[1];

        SleepDetailedAggregationDto aggregation = querySleepStatisticsPort.aggregateDetailedStatistics(user.id(), start, end);
        int todayMinutes = querySleepStatisticsPort.sumMinutesByDate(user.id(), date);
        List<SleepSessionDto> sessions = querySleepStatisticsPort.findByUserIdAndPeriod(user.id(), start, end);

        double sleepConsistency = calculateConsistencyScore(sessions, aggregation);
        LocalTime averageBedTime = convertMinutesToLocalTime(aggregation.averageBedTimeMinutes());
        LocalTime averageWakeTime = convertMinutesToLocalTime(aggregation.averageWakeTimeMinutes());

        SleepStatistics statistics = new SleepStatistics(
                todayMinutes,
                aggregation.averageMinutes(),
                sleepConsistency,
                averageBedTime,
                averageWakeTime,
                aggregation.totalMinutes()
        );

        return SleepStatisticsResponse.from(statistics);
    }

    private LocalDateTime[] calculatePeriodRange(SleepPeriodType period, LocalDate date) {
        return StatisticsPeriodRangeCalculator.calculateSleepPeriodRange(period, date);
    }

    private double calculateConsistencyScore(List<SleepSessionDto> sessions, SleepDetailedAggregationDto aggregation) {
        if (sessions.size() < MIN_SESSIONS_FOR_CONSISTENCY) {
            return MAX_CONSISTENCY_SCORE;
        }

        double variance = calculateBedTimeVariance(sessions, aggregation.averageBedTimeMinutes());

        double score = Math.max(
                MIN_CONSISTENCY_SCORE,
                MAX_CONSISTENCY_SCORE - (variance / VARIANCE_TO_SCORE_DIVISOR * VARIANCE_TO_SCORE_MULTIPLIER)
        );
        
        return Math.round(score * SCORE_ROUNDING_PRECISION) / SCORE_ROUNDING_PRECISION;
    }

    private double calculateBedTimeVariance(List<SleepSessionDto> sessions, int avgBedMinutes) {
        if (sessions.isEmpty()) {
            return 0.0;
        }

        double sumSquaredDiff = sessions.stream()
                .mapToDouble(session -> {
                    int bedMinutes = session.startedAt().getHour() * TimeConstants.MINUTES_PER_HOUR 
                            + session.startedAt().getMinute();
                    double diff = bedMinutes - avgBedMinutes;
                    return diff * diff;
                })
                .average()
                .orElse(0.0);

        return Math.sqrt(sumSquaredDiff);
    }

    private LocalTime convertMinutesToLocalTime(int totalMinutes) {
        int hours = totalMinutes / TimeConstants.MINUTES_PER_HOUR;
        int minutes = totalMinutes % TimeConstants.MINUTES_PER_HOUR;
        return LocalTime.of(hours, minutes);
    }
}
