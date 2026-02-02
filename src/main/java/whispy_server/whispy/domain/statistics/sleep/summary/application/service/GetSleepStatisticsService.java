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
        int totalCount = aggregation.totalCount();

        SleepStatistics statistics = new SleepStatistics(
                todayMinutes,
                aggregation.averageMinutes(),
                sleepConsistency,
                averageBedTime,
                averageWakeTime,
                aggregation.totalMinutes(),
                totalCount
        );

        return SleepStatisticsResponse.from(statistics);
    }

    /**
     * 수면 통계 조회 기간을 계산합니다.
     *
     * 기간 타입(DAILY, WEEKLY, MONTHLY)에 따라 시작일시와 종료일시를 계산하며,
     * 수면 기록의 특성상 전날 밤부터 당일 아침까지를 하나의 단위로 처리합니다.
     *
     * @param period 통계 기간 타입 (일간/주간/월간)
     * @param date   기준 날짜
     * @return [시작일시, 종료일시] 배열
     */
    private LocalDateTime[] calculatePeriodRange(SleepPeriodType period, LocalDate date) {
        return StatisticsPeriodRangeCalculator.calculateSleepPeriodRange(period, date);
    }

    /**
     * 수면 일관성 점수를 계산합니다.
     *
     * 취침 시간의 분산(variance)을 기반으로 수면 패턴의 규칙성을 0~100점으로 환산합니다.
     * 분산이 클수록(취침 시간이 불규칙할수록) 점수가 낮아집니다.
     *
     * 계산 로직:
     * - 세션이 2개 미만: 100점 (비교 불가)
     * - 분산 기반 점수: 100 - (분산 / 120 * 50)
     * - 최소 0점, 최대 100점으로 제한
     * - 소수점 첫째 자리까지 반올림
     *
     * @param sessions    수면 세션 목록
     * @param aggregation 집계 데이터 (평균 취침 시간 포함)
     * @return 수면 일관성 점수 (0~100)
     */
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

    /**
     * 취침 시간의 분산(variance)을 계산합니다.
     *
     * 각 수면 세션의 취침 시간(startedAt)을 자정 이후 분(minutes) 단위로 변환한 후,
     * 평균 취침 시간과의 차이를 제곱하여 평균낸 값의 제곱근(표준편차)을 반환합니다.
     *
     * 예시:
     * - 평균 취침 시간: 23:00 (1380분)
     * - 세션 1: 22:50 (1370분) → 차이 -10분
     * - 세션 2: 23:10 (1390분) → 차이 +10분
     * - 분산: sqrt(((−10)² + (10)²) / 2) ≈ 10분
     *
     * @param sessions       수면 세션 목록
     * @param avgBedMinutes  평균 취침 시간 (자정 이후 분 단위)
     * @return 취침 시간의 표준편차 (분 단위)
     */
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

    /**
     * 자정 이후 총 분(minutes)을 LocalTime 객체로 변환합니다.
     *
     * DB에 저장된 시간 데이터는 자정(00:00) 이후 경과한 총 분으로 저장되므로,
     * 이를 시(hour)와 분(minute)으로 분해하여 LocalTime 객체로 변환합니다.
     *
     * 예시:
     * - 1380분 → 23:00 (1380 / 60 = 23시간, 1380 % 60 = 0분)
     * - 90분 → 01:30 (90 / 60 = 1시간, 90 % 60 = 30분)
     *
     * @param totalMinutes 자정 이후 경과한 총 분
     * @return LocalTime 객체
     */
    private LocalTime convertMinutesToLocalTime(int totalMinutes) {
        int hours = totalMinutes / TimeConstants.MINUTES_PER_HOUR;
        int minutes = totalMinutes % TimeConstants.MINUTES_PER_HOUR;
        return LocalTime.of(hours, minutes);
    }
}
