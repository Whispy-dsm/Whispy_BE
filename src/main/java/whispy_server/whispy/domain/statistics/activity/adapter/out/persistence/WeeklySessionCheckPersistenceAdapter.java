package whispy_server.whispy.domain.statistics.activity.adapter.out.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.focussession.adapter.out.entity.QFocusSessionJpaEntity;
import whispy_server.whispy.domain.sleepsession.adapter.out.entity.QSleepSessionJpaEntity;
import whispy_server.whispy.domain.statistics.activity.applicatoin.port.out.CheckWeeklySessionExistsPort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WeeklySessionCheckPersistenceAdapter implements CheckWeeklySessionExistsPort {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Set<LocalDate> findSessionDatesInPeriod(Long userId, LocalDateTime start, LocalDateTime end) {
        QFocusSessionJpaEntity focusSession = QFocusSessionJpaEntity.focusSessionJpaEntity;
        QSleepSessionJpaEntity sleepSession = QSleepSessionJpaEntity.sleepSessionJpaEntity;

        Set<LocalDate> focusDates = jpaQueryFactory
                .select(focusSession.startedAt)
                .from(focusSession)
                .where(
                        focusSession.userId.eq(userId),
                        focusSession.startedAt.between(start, end)
                )
                .fetch()
                .stream()
                .map(LocalDateTime::toLocalDate)
                .collect(Collectors.toSet());

        Set<LocalDate> sleepDates = jpaQueryFactory
                .select(sleepSession.startedAt)
                .from(sleepSession)
                .where(
                        sleepSession.userId.eq(userId),
                        sleepSession.startedAt.between(start, end)
                )
                .fetch()
                .stream()
                .map(LocalDateTime::toLocalDate)
                .collect(Collectors.toSet());

        Set<LocalDate> allDates = new HashSet<>(focusDates);
        allDates.addAll(sleepDates);

        return allDates;
    }
}
