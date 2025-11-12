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

@Component
@RequiredArgsConstructor
public class FocusComparisonPersistenceAdapter implements QueryFocusComparisonPort {

    private final JPAQueryFactory jpaQueryFactory;

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
