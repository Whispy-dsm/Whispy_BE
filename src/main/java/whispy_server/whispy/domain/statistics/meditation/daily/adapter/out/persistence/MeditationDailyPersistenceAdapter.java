package whispy_server.whispy.domain.statistics.meditation.daily.adapter.out.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.meditationsession.adapter.out.entity.QMeditationSessionJpaEntity;
import whispy_server.whispy.domain.statistics.common.constants.TimeConstants;
import whispy_server.whispy.domain.statistics.meditation.daily.application.port.out.QueryMeditationStatisticsPort;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class MeditationDailyPersistenceAdapter implements QueryMeditationStatisticsPort {

    private final JPAQueryFactory jpaQueryFactory;

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
