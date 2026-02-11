package whispy_server.whispy.domain.statistics.focus.daily.adapter.out.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.DailyFocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.DailyTagFocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.HourlyFocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.HourlyTagFocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.MonthlyFocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.MonthlyTagFocusAggregationDto;
import whispy_server.whispy.domain.focussession.adapter.out.entity.QFocusSessionJpaEntity;
import whispy_server.whispy.domain.statistics.common.constants.TimeConstants;
import whispy_server.whispy.domain.statistics.focus.summary.adapter.out.dto.TagMinutesDto;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.focus.FocusSessionDto;
import whispy_server.whispy.domain.statistics.focus.daily.application.port.out.QueryFocusStatisticsPort;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 집중 일일 통계 영속성 어댑터.
 *
 * QueryDSL을 사용하여 집중 세션 데이터를 조회하고 집계하는 아웃바운드 어댑터입니다.
 */
@Component
@RequiredArgsConstructor
public class FocusDailyPersistenceAdapter implements QueryFocusStatisticsPort {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 기간 내 사용자의 집중 세션을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 집중 세션 DTO 목록
     */
    @Override
    public List<FocusSessionDto> findByUserIdAndPeriod(Long userId, LocalDateTime start, LocalDateTime end) {
        QFocusSessionJpaEntity focusSession = QFocusSessionJpaEntity.focusSessionJpaEntity;

        return jpaQueryFactory
                .select(Projections.constructor(
                        FocusSessionDto.class,
                        focusSession.id,
                        focusSession.userId,
                        focusSession.startedAt,
                        focusSession.endedAt,
                        focusSession.durationSeconds,
                        focusSession.tag,
                        focusSession.createdAt
                ))
                .from(focusSession)
                .where(
                        focusSession.userId.eq(userId),
                        focusSession.startedAt.between(start, end)
                )
                .orderBy(focusSession.startedAt.desc())
                .fetch();
    }

    @Override
    public List<HourlyFocusAggregationDto> aggregateHourlyMinutes(Long userId, LocalDateTime start, LocalDateTime end) {
        QFocusSessionJpaEntity focusSession = QFocusSessionJpaEntity.focusSessionJpaEntity;

        return jpaQueryFactory
                .select(Projections.constructor(
                        HourlyFocusAggregationDto.class,
                        focusSession.startedAt.hour(),
                        focusSession.durationSeconds.sum().divide(TimeConstants.SECONDS_PER_MINUTE).intValue()
                ))
                .from(focusSession)
                .where(
                        focusSession.userId.eq(userId),
                        focusSession.startedAt.between(start, end)
                )
                .groupBy(focusSession.startedAt.hour())
                .orderBy(focusSession.startedAt.hour().asc())
                .fetch();
    }

    @Override
    public List<DailyFocusAggregationDto> aggregateDailyMinutes(Long userId, LocalDateTime start, LocalDateTime end) {
        QFocusSessionJpaEntity focusSession = QFocusSessionJpaEntity.focusSessionJpaEntity;

        return jpaQueryFactory
                .select(Projections.constructor(
                        DailyFocusAggregationDto.class,
                        Expressions.dateTemplate(Date.class, "DATE({0})", focusSession.startedAt),
                        focusSession.durationSeconds.sum().divide(TimeConstants.SECONDS_PER_MINUTE).intValue()
                ))
                .from(focusSession)
                .where(
                        focusSession.userId.eq(userId),
                        focusSession.startedAt.between(start, end)
                )
                .groupBy(Expressions.dateTemplate(Date.class, "DATE({0})", focusSession.startedAt))
                .orderBy(Expressions.dateTemplate(Date.class, "DATE({0})", focusSession.startedAt).asc())
                .fetch();
    }

    @Override
    public List<MonthlyFocusAggregationDto> aggregateMonthlyMinutes(Long userId, int year) {
        QFocusSessionJpaEntity focusSession = QFocusSessionJpaEntity.focusSessionJpaEntity;

        LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(year, 12, 31, 23, 59, 59);

        return jpaQueryFactory
                .select(Projections.constructor(
                        MonthlyFocusAggregationDto.class,
                        focusSession.startedAt.month(),
                        focusSession.durationSeconds.sum().divide(TimeConstants.SECONDS_PER_MINUTE).intValue()
                ))
                .from(focusSession)
                .where(
                        focusSession.userId.eq(userId),
                        focusSession.startedAt.between(start, end)
                )
                .groupBy(focusSession.startedAt.month())
                .orderBy(focusSession.startedAt.month().asc())
                .fetch();
    }

    @Override
    public int getTotalMinutes(Long userId, LocalDateTime start, LocalDateTime end) {
        QFocusSessionJpaEntity focusSession = QFocusSessionJpaEntity.focusSessionJpaEntity;

        Integer totalSeconds = jpaQueryFactory
                .select(focusSession.durationSeconds.sum())
                .from(focusSession)
                .where(
                        focusSession.userId.eq(userId),
                        focusSession.startedAt.between(start, end)
                )
                .fetchOne();

        return totalSeconds != null ? totalSeconds / TimeConstants.SECONDS_PER_MINUTE : 0;
    }

    @Override
    public List<TagMinutesDto> aggregateByTag(Long userId, LocalDateTime start, LocalDateTime end) {
        QFocusSessionJpaEntity focusSession = QFocusSessionJpaEntity.focusSessionJpaEntity;

        return jpaQueryFactory
                .select(Projections.constructor(
                        TagMinutesDto.class,
                        focusSession.tag,
                        focusSession.durationSeconds.sum().divide(60).intValue()
                ))
                .from(focusSession)
                .where(
                        focusSession.userId.eq(userId),
                        focusSession.startedAt.between(start, end)
                )
                .groupBy(focusSession.tag)
                .fetch();
    }

    @Override
    public List<HourlyTagFocusAggregationDto> aggregateHourlyByTag(Long userId, LocalDateTime start, LocalDateTime end) {
        QFocusSessionJpaEntity focusSession = QFocusSessionJpaEntity.focusSessionJpaEntity;

        return jpaQueryFactory
                .select(Projections.constructor(
                        HourlyTagFocusAggregationDto.class,
                        focusSession.startedAt.hour(),
                        focusSession.tag,
                        focusSession.durationSeconds.sum().divide(TimeConstants.SECONDS_PER_MINUTE).intValue()
                ))
                .from(focusSession)
                .where(
                        focusSession.userId.eq(userId),
                        focusSession.startedAt.between(start, end)
                )
                .groupBy(focusSession.startedAt.hour(), focusSession.tag)
                .orderBy(focusSession.startedAt.hour().asc(), focusSession.tag.asc())
                .fetch();
    }

    @Override
    public List<DailyTagFocusAggregationDto> aggregateDailyByTag(Long userId, LocalDateTime start, LocalDateTime end) {
        QFocusSessionJpaEntity focusSession = QFocusSessionJpaEntity.focusSessionJpaEntity;

        return jpaQueryFactory
                .select(Projections.constructor(
                        DailyTagFocusAggregationDto.class,
                        Expressions.dateTemplate(Date.class, "DATE({0})", focusSession.startedAt),
                        focusSession.tag,
                        focusSession.durationSeconds.sum().divide(TimeConstants.SECONDS_PER_MINUTE).intValue()
                ))
                .from(focusSession)
                .where(
                        focusSession.userId.eq(userId),
                        focusSession.startedAt.between(start, end)
                )
                .groupBy(Expressions.dateTemplate(Date.class, "DATE({0})", focusSession.startedAt), focusSession.tag)
                .orderBy(Expressions.dateTemplate(Date.class, "DATE({0})", focusSession.startedAt).asc(), focusSession.tag.asc())
                .fetch();
    }

    @Override
    public List<MonthlyTagFocusAggregationDto> aggregateMonthlyByTag(Long userId, int year) {
        QFocusSessionJpaEntity focusSession = QFocusSessionJpaEntity.focusSessionJpaEntity;

        LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(year, 12, 31, 23, 59, 59);

        return jpaQueryFactory
                .select(Projections.constructor(
                        MonthlyTagFocusAggregationDto.class,
                        focusSession.startedAt.month(),
                        focusSession.tag,
                        focusSession.durationSeconds.sum().divide(TimeConstants.SECONDS_PER_MINUTE).intValue()
                ))
                .from(focusSession)
                .where(
                        focusSession.userId.eq(userId),
                        focusSession.startedAt.between(start, end)
                )
                .groupBy(focusSession.startedAt.month(), focusSession.tag)
                .orderBy(focusSession.startedAt.month().asc(), focusSession.tag.asc())
                .fetch();
    }
}
