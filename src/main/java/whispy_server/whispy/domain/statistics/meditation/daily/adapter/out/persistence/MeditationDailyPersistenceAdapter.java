package whispy_server.whispy.domain.statistics.meditation.daily.adapter.out.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.meditationsession.adapter.out.entity.QMeditationSessionJpaEntity;
import whispy_server.whispy.domain.statistics.common.constants.TimeConstants;
import whispy_server.whispy.domain.statistics.meditation.daily.application.port.out.QueryMeditationStatisticsPort;

import java.time.LocalDateTime;

/**
 * 명상 일일 통계 영속성 어댑터.
 *
 * QueryDSL을 사용하여 명상 세션 데이터를 조회하는 아웃바운드 어댑터입니다.
 */
@Component
@RequiredArgsConstructor
public class MeditationDailyPersistenceAdapter implements QueryMeditationStatisticsPort {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 기간 내 총 명상 시간(분)을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 총 명상 시간(분)
     */
    @Override
    public int getTotalMinutes(Long userId, LocalDateTime start, LocalDateTime end) {
        QMeditationSessionJpaEntity meditationSession = QMeditationSessionJpaEntity.meditationSessionJpaEntity;

        Integer totalSeconds = jpaQueryFactory
                .select(meditationSession.durationSeconds.sum())
                .from(meditationSession)
                .where(
                        meditationSession.userId.eq(userId),
                        meditationSession.startedAt.between(start, end)
                )
                .fetchOne();

        return totalSeconds != null ? totalSeconds / TimeConstants.SECONDS_PER_MINUTE : 0;
    }
}
