package whispy_server.whispy.domain.statistics.shared.adapter.out.dto.focus;

import whispy_server.whispy.domain.focussession.model.types.FocusTag;

import java.time.LocalDateTime;

/**
 * 집중 세션 DTO.
 *
 * 통계 조회를 위한 집중 세션 정보입니다.
 *
 * @param id 세션 ID
 * @param userId 사용자 ID
 * @param startedAt 시작 시간
 * @param endedAt 종료 시간
 * @param durationSeconds 세션 지속 시간(초)
 * @param tag 집중 태그
 * @param createdAt 생성 시간
 */
public record FocusSessionDto(
        Long id,
        Long userId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        int durationSeconds,
        FocusTag tag,
        LocalDateTime createdAt
) {
}
