package whispy_server.whispy.domain.statistics.meditation.daily.application.port.out;

import java.time.LocalDateTime;

/**
 * 명상 통계 조회 포트.
 *
 * 명상 통계 조회를 위한 아웃바운드 포트 인터페이스입니다.
 */
public interface QueryMeditationStatisticsPort {
    /**
     * 기간 내 총 명상 시간(분)을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 총 명상 시간(분)
     */
    int getTotalMinutes(Long userId, LocalDateTime start, LocalDateTime end);
}
