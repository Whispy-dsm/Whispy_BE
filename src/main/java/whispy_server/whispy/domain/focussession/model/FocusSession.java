package whispy_server.whispy.domain.focussession.model;

import whispy_server.whispy.domain.focussession.model.types.FocusTag;
import whispy_server.whispy.global.annotation.Aggregate;
import whispy_server.whispy.global.exception.domain.focussession.FocusSessionDurationExceededException;
import whispy_server.whispy.global.exception.domain.focussession.InvalidFocusSessionDurationException;
import whispy_server.whispy.global.exception.domain.focussession.InvalidFocusSessionTimeRangeException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 집중 세션 도메인 모델 (애그리게잇).
 *
 * 사용자의 집중 활동 기록의 핵심 정보를 담고 있는 도메인 모델입니다.
 * 집중 세션은 시작 시간, 종료 시간, 지속 시간 및 활동 태그로 구성되며,
 * 세션 생성 시 시간 범위의 유효성과 지속 시간의 일관성을 검증합니다.
 *
 * @param id 집중 세션 ID
 * @param userId 세션을 기록한 사용자 ID
 * @param startedAt 집중 시작 일시
 * @param endedAt 집중 종료 일시
 * @param durationSeconds 집중 지속 시간(초 단위)
 * @param tag 집중 활동의 분류 태그
 * @param createdAt 세션 생성 일시
 */
@Aggregate
public record FocusSession(
        Long id,
        Long userId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        int durationSeconds,
        FocusTag tag,
        LocalDateTime createdAt
) {
    public FocusSession {
        if (endedAt.isBefore(startedAt) || endedAt.isEqual(startedAt)) {
            throw InvalidFocusSessionTimeRangeException.EXCEPTION;
        }

        long actualSeconds = ChronoUnit.SECONDS.between(startedAt, endedAt);
        if (durationSeconds > actualSeconds) {
            throw FocusSessionDurationExceededException.EXCEPTION;
        }

        if (durationSeconds <= 0) {
            throw InvalidFocusSessionDurationException.EXCEPTION;
        }
    }
}