package whispy_server.whispy.domain.statistics.sleep.comparison.adapter.out.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.sleepsession.adapter.out.entity.QSleepSessionJpaEntity;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.sleep.SleepSessionDto;
import whispy_server.whispy.domain.statistics.sleep.comparison.application.port.out.QuerySleepComparisonPort;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SleepComparisonPersistenceAdapter implements QuerySleepComparisonPort {

    private final JPAQueryFactory jpaQueryFactory;

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
}
