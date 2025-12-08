package whispy_server.whispy.domain.statistics.sleep.daily.adapter.out.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.sleepsession.adapter.out.entity.QSleepSessionJpaEntity;
import whispy_server.whispy.domain.statistics.common.constants.TimeConstants;
import whispy_server.whispy.domain.statistics.sleep.daily.adapter.out.dto.DailySleepAggregationDto;
import whispy_server.whispy.domain.statistics.sleep.daily.adapter.out.dto.MonthlySleepAggregationDto;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.sleep.SleepSessionDto;
import whispy_server.whispy.domain.statistics.sleep.daily.application.port.out.QuerySleepStatisticsPort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 수면 일일 통계 영속성 어댑터.
 *
 * QueryDSL을 사용하여 수면 세션 데이터를 조회하고 집계하는 아웃바운드 어댑터입니다.
 */
@Component
@RequiredArgsConstructor
public class SleepDailyPersistenceAdapter implements QuerySleepStatisticsPort {

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
    public List<DailySleepAggregationDto> aggregateDailyMinutes(Long userId, LocalDateTime start, LocalDateTime end) {
        QSleepSessionJpaEntity sleepSession = QSleepSessionJpaEntity.sleepSessionJpaEntity;

        return jpaQueryFactory
                .select(Projections.constructor(
                        DailySleepAggregationDto.class,
                        Expressions.dateTemplate(LocalDate.class, "DATE({0})", sleepSession.startedAt),
                        sleepSession.durationSeconds.sum().divide(TimeConstants.SECONDS_PER_MINUTE).intValue()
                ))
                .from(sleepSession)
                .where(
                        sleepSession.userId.eq(userId),
                        sleepSession.startedAt.between(start, end)
                )
                .groupBy(Expressions.dateTemplate(LocalDate.class, "DATE({0})", sleepSession.startedAt))
                .orderBy(Expressions.dateTemplate(LocalDate.class, "DATE({0})", sleepSession.startedAt).asc())
                .fetch();
    }

    @Override
    public List<MonthlySleepAggregationDto> aggregateMonthlyMinutes(Long userId, int year) {
        QSleepSessionJpaEntity sleepSession = QSleepSessionJpaEntity.sleepSessionJpaEntity;

        LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(year, 12, 31, 23, 59, 59);

        return jpaQueryFactory
                .select(Projections.constructor(
                        MonthlySleepAggregationDto.class,
                        sleepSession.startedAt.month(),
                        sleepSession.durationSeconds.sum().divide(TimeConstants.SECONDS_PER_MINUTE).intValue()
                ))
                .from(sleepSession)
                .where(
                        sleepSession.userId.eq(userId),
                        sleepSession.startedAt.between(start, end)
                )
                .groupBy(sleepSession.startedAt.month())
                .orderBy(sleepSession.startedAt.month().asc())
                .fetch();
    }
}
