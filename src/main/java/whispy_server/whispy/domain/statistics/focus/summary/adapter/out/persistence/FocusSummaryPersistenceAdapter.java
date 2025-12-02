package whispy_server.whispy.domain.statistics.focus.summary.adapter.out.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.focussession.adapter.out.entity.QFocusSessionJpaEntity;
import whispy_server.whispy.domain.statistics.common.constants.TimeConstants;
import whispy_server.whispy.domain.statistics.focus.summary.adapter.out.dto.FocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.summary.application.port.out.QueryFocusStatisticsPort;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.focus.FocusSessionDto;
import whispy_server.whispy.domain.statistics.focus.summary.adapter.out.dto.TagMinutesDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 집중 요약 통계 영속성 어댑터.
 *
 * QueryDSL을 사용하여 집중 세션 데이터를 조회하고 요약 통계를 생성하는 아웃바운드 어댑터입니다.
 */
@Component
@RequiredArgsConstructor
public class FocusSummaryPersistenceAdapter implements QueryFocusStatisticsPort {

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
    public FocusAggregationDto aggregateByPeriod(Long userId, LocalDateTime start, LocalDateTime end) {
        QFocusSessionJpaEntity focusSession = QFocusSessionJpaEntity.focusSessionJpaEntity;

        FocusAggregationDto result = jpaQueryFactory
                .select(Projections.constructor(
                        FocusAggregationDto.class,
                        focusSession.count().intValue(),
                        focusSession.durationSeconds.sum().divide(60).coalesce(0).intValue()
                ))
                .from(focusSession)
                .where(
                        focusSession.userId.eq(userId),
                        focusSession.startedAt.between(start, end)
                )
                .fetchOne();

        return result != null ? result : new FocusAggregationDto(0, 0);
    }

    @Override
    public int sumMinutesByDate(Long userId, LocalDate date) {
        QFocusSessionJpaEntity focusSession = QFocusSessionJpaEntity.focusSessionJpaEntity;

        Integer result = jpaQueryFactory
                .select(focusSession.durationSeconds.sum().divide(60).coalesce(0).intValue())
                .from(focusSession)
                .where(
                        focusSession.userId.eq(userId),
                        focusSession.startedAt.between(
                                date.atStartOfDay(),
                                date.atTime(TimeConstants.END_OF_DAY)
                        )
                )
                .fetchOne();

        return result != null ? result : 0;
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
}
