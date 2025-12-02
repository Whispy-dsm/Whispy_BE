package whispy_server.whispy.domain.sleepsession.model;

import whispy_server.whispy.global.annotation.Aggregate;
import whispy_server.whispy.global.exception.domain.sleepsession.InvalidSleepSessionDurationException;
import whispy_server.whispy.global.exception.domain.sleepsession.InvalidSleepSessionTimeRangeException;
import whispy_server.whispy.global.exception.domain.sleepsession.SleepSessionDurationExceededException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 수면 세션 도메인 모델 (애그리게잇).
 *
 * 사용자의 수면 기록의 핵심 정보를 담고 있는 도메인 모델입니다.
 * 수면 세션은 시작 시간과 종료 시간, 지속 시간으로 구성되며,
 * 세션 생성 시 시간 범위의 유효성과 지속 시간의 일관성을 검증합니다.
 *
 * @param id 수면 세션 ID
 * @param userId 세션을 기록한 사용자 ID
 * @param startedAt 수면 시작 일시
 * @param endedAt 수면 종료 일시
 * @param durationSeconds 수면 지속 시간(초 단위)
 * @param createdAt 세션 생성 일시
 */
@Aggregate
public record SleepSession(
        Long id,
        Long userId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        int durationSeconds,
        LocalDateTime createdAt
) {
    public SleepSession {
        if (endedAt.isBefore(startedAt) || endedAt.isEqual(startedAt)) {
            throw InvalidSleepSessionTimeRangeException.EXCEPTION;
        }

        long actualSeconds = ChronoUnit.SECONDS.between(startedAt, endedAt);
        if (durationSeconds > actualSeconds) {
            throw SleepSessionDurationExceededException.EXCEPTION;
        }

        if (durationSeconds <= 0) {
            throw InvalidSleepSessionDurationException.EXCEPTION;
        }
    }
}
