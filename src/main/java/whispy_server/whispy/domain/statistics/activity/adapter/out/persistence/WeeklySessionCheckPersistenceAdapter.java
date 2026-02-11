package whispy_server.whispy.domain.statistics.activity.adapter.out.persistence;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.focussession.adapter.out.entity.QFocusSessionJpaEntity;
import whispy_server.whispy.domain.meditationsession.adapter.out.entity.QMeditationSessionJpaEntity;
import whispy_server.whispy.domain.sleepsession.adapter.out.entity.QSleepSessionJpaEntity;
import whispy_server.whispy.domain.statistics.activity.applicatoin.port.out.ActivityPort;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 주간 세션 확인 영속성 어댑터.
 *
 * QueryDSL을 사용하여 주간 세션 데이터를 조회하는 아웃바운드 어댑터입니다.
 */
@Component
@RequiredArgsConstructor
public class WeeklySessionCheckPersistenceAdapter implements ActivityPort {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 기간 내 세션이 있는 날짜들을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 기간 내 세션이 있는 날짜 집합
     */
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

    /**
     * 기간 내 요일별 누적 세션 시간(분)을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 요일별 누적 세션 시간 맵
     */
    @Override
    public Map<LocalDate, Integer> findSessionMinutesInPeriod(Long userId, LocalDateTime start, LocalDateTime end) {
        QFocusSessionJpaEntity focusSession = QFocusSessionJpaEntity.focusSessionJpaEntity;
        QSleepSessionJpaEntity sleepSession = QSleepSessionJpaEntity.sleepSessionJpaEntity;

        Map<LocalDate, Integer> result = new HashMap<>();

        aggregateFocusMinutes(userId, start, end, focusSession, result);
        aggregateSleepMinutes(userId, start, end, sleepSession, result);

        return result;
    }

    /**
     * 집중 세션의 날짜별 누적 시간을 집계합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @param focusSession 집중 세션 QueryDSL 엔티티
     * @param result 집계 결과를 저장할 맵
     */
    private void aggregateFocusMinutes(
            Long userId,
            LocalDateTime start,
            LocalDateTime end,
            QFocusSessionJpaEntity focusSession,
            Map<LocalDate, Integer> result
    ) {
        Expression<Date> dateExpression = toDateExpression(focusSession.startedAt);

        jpaQueryFactory
                .select(dateExpression, focusSession.durationSeconds.sum())
                .from(focusSession)
                .where(
                        focusSession.userId.eq(userId),
                        focusSession.startedAt.between(start, end)
                )
                .groupBy(dateExpression)
                .fetch()
                .forEach(tuple ->
                    mergeMinutesToResult(tuple, dateExpression, focusSession.durationSeconds.sum(), result)
                );
    }

    /**
     * 수면 세션의 날짜별 누적 시간을 집계합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @param sleepSession 수면 세션 QueryDSL 엔티티
     * @param result 집계 결과를 저장할 맵
     */
    private void aggregateSleepMinutes(
            Long userId,
            LocalDateTime start,
            LocalDateTime end,
            QSleepSessionJpaEntity sleepSession,
            Map<LocalDate, Integer> result
    ) {
        Expression<Date> dateExpression = toDateExpression(sleepSession.startedAt);

        jpaQueryFactory
                .select(dateExpression, sleepSession.durationSeconds.sum())
                .from(sleepSession)
                .where(
                        sleepSession.userId.eq(userId),
                        sleepSession.startedAt.between(start, end)
                )
                .groupBy(dateExpression)
                .fetch()
                .forEach(tuple ->
                    mergeMinutesToResult(tuple, dateExpression, sleepSession.durationSeconds.sum(), result)
                );
    }

    /**
     * 명상 세션의 날짜별 누적 시간을 집계합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @param meditation 명상 세션 QueryDSL 엔티티
     * @param result 집계 결과를 저장할 맵
     */
    private void aggregateMeditationMinutes(
            Long userId,
            LocalDateTime start,
            LocalDateTime end,
            QMeditationSessionJpaEntity meditation,
            Map<LocalDate, Integer> result
    ) {
        Expression<Date> dateExpression = toDateExpression(meditation.startedAt);

        jpaQueryFactory
                .select(dateExpression, meditation.durationSeconds.sum())
                .from(meditation)
                .where(
                        meditation.userId.eq(userId),
                        meditation.startedAt.between(start, end)
                )
                .groupBy(dateExpression)
                .fetch()
                .forEach(tuple ->
                    mergeMinutesToResult(tuple, dateExpression, meditation.durationSeconds.sum(), result)
                );
    }

    /**
     * LocalDateTime 경로를 LocalDate Expression으로 변환합니다.
     *
     * @param dateTimePath LocalDateTime 경로
     * @return DATE() 함수를 적용한 LocalDate Expression
     */
    private Expression<Date> toDateExpression(DateTimePath<LocalDateTime> dateTimePath) {
        return Expressions.dateTemplate(Date.class, "DATE({0})", dateTimePath);
    }

    /**
     * 쿼리 결과를 결과 맵에 병합합니다.
     *
     * @param tuple QueryDSL 쿼리 결과 튜플
     * @param dateExpression 날짜 Expression
     * @param durationSumExpression 지속 시간 합계 Expression
     * @param result 집계 결과를 저장할 맵
     */
    private void mergeMinutesToResult(
            Tuple tuple,
            Expression<Date> dateExpression,
            Expression<Integer> durationSumExpression,
            Map<LocalDate, Integer> result
    ) {
        Date sqlDate = tuple.get(dateExpression);

        if (sqlDate == null) {
            return;
        }

        LocalDate date = sqlDate.toLocalDate();
        Integer seconds = tuple.get(durationSumExpression);
        int minutes = seconds != null ? seconds / 60 : 0;
        result.merge(date, minutes, Integer::sum);
    }
}
