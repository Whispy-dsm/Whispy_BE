package whispy_server.whispy.domain.statistics.shared.adapter.out.dto.sleep;

import java.time.LocalDateTime;

/**
 * 수면 세션 DTO.
 *
 * 통계 조회를 위한 수면 세션 정보입니다.
 *
 * @param id 세션 ID
 * @param userId 사용자 ID
 * @param startedAt 시작 시간
 * @param endedAt 종료 시간
 * @param durationSeconds 세션 지속 시간(초)
 * @param createdAt 생성 시간
 */
public record SleepSessionDto(
        Long id,
        Long userId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        int durationSeconds,
        LocalDateTime createdAt
) {
}
