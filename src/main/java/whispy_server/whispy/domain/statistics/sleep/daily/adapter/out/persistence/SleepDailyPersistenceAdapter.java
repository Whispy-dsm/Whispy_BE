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

@Component
@RequiredArgsConstructor
public class SleepDailyPersistenceAdapter implements QuerySleepStatisticsPort {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SleepSessionDto> findByUserIdAndPeriod(Long userId, LocalDateTime start, LocalDateTime end) {
        QSleepSessionJpaEntity sleepSession = QSleepSessionJpaEntity.sleepSessionJpaEntity;

        return jpaQueryFactory
                .select(Projections.constructor(
                        SleepSessionDto.class,
                        sleepSession.id,
                        sleepSession.userId,
                        sleepSession.musicId,
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
