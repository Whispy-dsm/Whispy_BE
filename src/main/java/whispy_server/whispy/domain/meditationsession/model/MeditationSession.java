package whispy_server.whispy.domain.meditationsession.model;

import whispy_server.whispy.domain.meditationsession.model.types.BreatheMode;
import whispy_server.whispy.global.annotation.Aggregate;
import whispy_server.whispy.global.exception.domain.meditationsession.InvalidMeditationSessionDurationException;
import whispy_server.whispy.global.exception.domain.meditationsession.InvalidMeditationSessionTimeRangeException;
import whispy_server.whispy.global.exception.domain.meditationsession.MeditationSessionDurationExceededException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 명상 세션 도메인 모델 (애그리게잇).
 *
 * 사용자의 명상 활동 기록의 핵심 정보를 담고 있는 도메인 모델입니다.
 * 명상 세션은 시작 시간, 종료 시간, 지속 시간 및 호흡 모드로 구성되며,
 * 세션 생성 시 시간 범위의 유효성과 지속 시간의 일관성을 검증합니다.
 *
 * @param id 명상 세션 ID
 * @param userId 세션을 기록한 사용자 ID
 * @param startedAt 명상 시작 일시
 * @param endedAt 명상 종료 일시
 * @param durationSeconds 명상 지속 시간(초 단위)
 * @param breatheMode 명상 시 사용한 호흡 모드
 * @param createdAt 세션 생성 일시
 */
@Aggregate
public record MeditationSession(
        Long id,
        Long userId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        int durationSeconds,
        BreatheMode breatheMode,
        LocalDateTime createdAt
) {
    public MeditationSession {
        if (endedAt.isBefore(startedAt) || endedAt.isEqual(startedAt)) {
            throw InvalidMeditationSessionTimeRangeException.EXCEPTION;
        }

        long actualSeconds = ChronoUnit.SECONDS.between(startedAt, endedAt);
        if (durationSeconds > actualSeconds) {
            throw MeditationSessionDurationExceededException.EXCEPTION;
        }

        if (durationSeconds <= 0) {
            throw InvalidMeditationSessionDurationException.EXCEPTION;
        }
    }
}
