package whispy_server.whispy.domain.statistics.sleep.summary.adapter.out.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.sleepsession.adapter.out.entity.QSleepSessionJpaEntity;
import whispy_server.whispy.domain.statistics.common.constants.TimeConstants;
import whispy_server.whispy.domain.statistics.sleep.summary.adapter.out.dto.SleepAggregationDto;
import whispy_server.whispy.domain.statistics.sleep.summary.adapter.out.dto.SleepDetailedAggregationDto;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.sleep.SleepSessionDto;
import whispy_server.whispy.domain.statistics.sleep.summary.application.port.out.QuerySleepStatisticsPort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 수면 요약 통계 영속성 어댑터.
 *
 * QueryDSL을 사용하여 수면 세션 데이터를 조회하고 요약 통계를 생성하는 아웃바운드 어댑터입니다.
 */
@Component
@RequiredArgsConstructor
public class SleepSummaryPersistenceAdapter implements QuerySleepStatisticsPort {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 기간 내 사용자의 수면 세션을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 수면 세션 DTO 목록
     */
    @Override
    public List<SleepSessionDto> findByUserIdAndPeriod(Long userId, LocalDateTime start, LocalDateTime end) {
        QSleepSessionJpaEntity sleepSession = QSleepSessionJpaEntity.sleepSessionJpaEntity;

        return jpaQueryFactory
                .select(Projections.constructor(
                        SleepSessionDto.class,
                        sleepSession.id,
                        sleepSession.userId,
                        sleepSession.startedAt,
                        sleepSession.endedAt,
                        sleepSession.durationSeconds,
                        sleepSession.createdAt
                ))
                .from(sleepSession)
                .where(
                        sleepSession.userId.eq(userId),
                        sleepSession.startedAt.between(start, end)
                )
                .orderBy(sleepSession.startedAt.desc())
                .fetch();
    }

    @Override
    public SleepAggregationDto aggregateByPeriod(Long userId, LocalDateTime start, LocalDateTime end) {
        QSleepSessionJpaEntity sleepSession = QSleepSessionJpaEntity.sleepSessionJpaEntity;

        SleepAggregationDto result = jpaQueryFactory
                .select(Projections.constructor(
                        SleepAggregationDto.class,
                        sleepSession.count().intValue(),
                        sleepSession.durationSeconds.sum().divide(60).coalesce(0).intValue()
                ))
                .from(sleepSession)
                .where(
                        sleepSession.userId.eq(userId),
                        sleepSession.startedAt.between(start, end)
                )
                .fetchOne();

        return result != null ? result : new SleepAggregationDto(0, 0);
    }

    @Override
    public int sumMinutesByDate(Long userId, LocalDate date) {
        QSleepSessionJpaEntity sleepSession = QSleepSessionJpaEntity.sleepSessionJpaEntity;

        Integer result = jpaQueryFactory
                .select(sleepSession.durationSeconds.sum().divide(60).coalesce(0).intValue())
                .from(sleepSession)
                .where(
                        sleepSession.userId.eq(userId),
                        sleepSession.startedAt.between(
                                date.atStartOfDay(),
                                date.atTime(TimeConstants.END_OF_DAY)
                        )
                )
                .fetchOne();

        return result != null ? result : 0;
    }

    @Override
    public SleepDetailedAggregationDto aggregateDetailedStatistics(Long userId, LocalDateTime start, LocalDateTime end) {
        QSleepSessionJpaEntity sleepSession = QSleepSessionJpaEntity.sleepSessionJpaEntity;

        NumberExpression<Integer> startMinutesFromMidnight = 
                sleepSession.startedAt.hour().multiply(TimeConstants.MINUTES_PER_HOUR)
                        .add(sleepSession.startedAt.minute());
        
        NumberExpression<Integer> endMinutesFromMidnight = 
                sleepSession.endedAt.hour().multiply(TimeConstants.MINUTES_PER_HOUR)
                        .add(sleepSession.endedAt.minute());

        SleepDetailedAggregationDto result = jpaQueryFactory
                .select(Projections.constructor(
                        SleepDetailedAggregationDto.class,
                        sleepSession.count().intValue(),
                        sleepSession.durationSeconds.sum().divide(TimeConstants.SECONDS_PER_MINUTE).coalesce(0).intValue(),
                        sleepSession.durationSeconds.avg().divide(TimeConstants.SECONDS_PER_MINUTE).coalesce(0.0).intValue(),
                        startMinutesFromMidnight.avg().coalesce(0.0).intValue(),
                        endMinutesFromMidnight.avg().coalesce(0.0).intValue()
                ))
                .from(sleepSession)
                .where(
                        sleepSession.userId.eq(userId),
                        sleepSession.startedAt.between(start, end)
                )
                .fetchOne();

        return result != null ? result : new SleepDetailedAggregationDto(0, 0, 0, 0, 0);
    }
}
