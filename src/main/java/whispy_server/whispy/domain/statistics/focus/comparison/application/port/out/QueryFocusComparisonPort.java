package whispy_server.whispy.domain.statistics.focus.comparison.application.port.out;

import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.focus.FocusSessionDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 집중 기간 비교 조회 포트.
 *
 * 집중 기간 비교를 위한 아웃바운드 포트 인터페이스입니다.
 */
public interface QueryFocusComparisonPort {
    /**
     * 기간 내 사용자의 집중 세션을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 집중 세션 DTO 목록
     */
    List<FocusSessionDto> findByUserIdAndPeriod(Long userId, LocalDateTime start, LocalDateTime end);
}
