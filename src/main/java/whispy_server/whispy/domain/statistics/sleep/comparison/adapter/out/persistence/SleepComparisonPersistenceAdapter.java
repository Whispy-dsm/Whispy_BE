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

/**
 * 수면 기간 비교 영속성 어댑터.
 *
 * QueryDSL을 사용하여 수면 세션 데이터를 조회하는 아웃바운드 어댑터입니다.
 */
@Component
@RequiredArgsConstructor
public class SleepComparisonPersistenceAdapter implements QuerySleepComparisonPort {

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
}
