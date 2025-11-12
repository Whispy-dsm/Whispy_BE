package whispy_server.whispy.domain.statistics.activity.adapter.out.persistence;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.focussession.adapter.out.entity.QFocusSessionJpaEntity;
import whispy_server.whispy.domain.sleepsession.adapter.out.entity.QSleepSessionJpaEntity;
import whispy_server.whispy.domain.statistics.activity.applicatoin.port.out.ActivityPort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WeeklySessionCheckPersistenceAdapter implements ActivityPort {

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

        jpaQueryFactory
                .select(sleepSession.startedAt)
                .from(sleepSession)
                .where(
                        sleepSession.userId.eq(userId),
                        sleepSession.startedAt.between(start, end)
                )
                .fetch()
                .stream()
                .map(LocalDateTime::toLocalDate)
                .forEach(focusDates::add);

        return focusDates;
    }

    @Override
    public Map<LocalDate, Integer> findSessionMinutesInPeriod(Long userId, LocalDateTime start, LocalDateTime end) {
        QFocusSessionJpaEntity focusSession = QFocusSessionJpaEntity.focusSessionJpaEntity;
        QSleepSessionJpaEntity sleepSession = QSleepSessionJpaEntity.sleepSessionJpaEntity;

        Map<LocalDate, Integer> result = new HashMap<>();

        aggregateFocusMinutes(userId, start, end, focusSession, result);
        aggregateSleepMinutes(userId, start, end, sleepSession, result);

        return result;
    }

    private void aggregateFocusMinutes(
            Long userId,
            LocalDateTime start,
            LocalDateTime end,
            QFocusSessionJpaEntity focusSession,
            Map<LocalDate, Integer> result
    ) {
        jpaQueryFactory
                .select(focusSession.startedAt, focusSession.durationSeconds.sum())
                .from(focusSession)
                .where(
                        focusSession.userId.eq(userId),
                        focusSession.startedAt.between(start, end)
                )
                .groupBy(Expressions.dateTemplate(LocalDate.class, "DATE({0})", focusSession.startedAt))
                .fetch()
                .forEach(tuple ->
                    mergeMinutesToResult(tuple, focusSession.startedAt, focusSession.durationSeconds.sum(), result)
                );
    }

    private void aggregateSleepMinutes(
            Long userId,
            LocalDateTime start,
            LocalDateTime end,
            QSleepSessionJpaEntity sleepSession,
            Map<LocalDate, Integer> result
    ) {
        jpaQueryFactory
                .select(sleepSession.startedAt, sleepSession.durationSeconds.sum())
                .from(sleepSession)
                .where(
                        sleepSession.userId.eq(userId),
                        sleepSession.startedAt.between(start, end)
                )
                .groupBy(Expressions.dateTemplate(LocalDate.class, "DATE({0})", sleepSession.startedAt))
                .fetch()
                .forEach(tuple ->
                    mergeMinutesToResult(tuple, sleepSession.startedAt, sleepSession.durationSeconds.sum(), result)
                );
    }

    private void mergeMinutesToResult(
            Tuple tuple,
            DateTimePath<LocalDateTime> startedAtPath,
            Expression<Integer> durationSumExpression,
            Map<LocalDate, Integer> result
    ) {
        LocalDate date = tuple.get(startedAtPath).toLocalDate();
        Integer seconds = tuple.get(durationSumExpression);
        int minutes = seconds != null ? seconds / 60 : 0;
        result.merge(date, minutes, Integer::sum);
    }
}
