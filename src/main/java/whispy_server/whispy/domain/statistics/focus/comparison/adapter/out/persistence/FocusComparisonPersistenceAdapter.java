package whispy_server.whispy.domain.statistics.focus.comparison.adapter.out.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.focussession.adapter.out.entity.QFocusSessionJpaEntity;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.focus.FocusSessionDto;
import whispy_server.whispy.domain.statistics.focus.comparison.application.port.out.QueryFocusComparisonPort;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 집중 기간 비교 영속성 어댑터.
 *
 * QueryDSL을 사용하여 집중 세션 데이터를 조회하는 아웃바운드 어댑터입니다.
 */
@Component
@RequiredArgsConstructor
public class FocusComparisonPersistenceAdapter implements QueryFocusComparisonPort {

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
}
