package whispy_server.whispy.domain.statistics.activity.applicatoin.port.out;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 활동 시간 조회 포트.
 *
 * 활동 누적 시간 조회를 위한 아웃바운드 포트 인터페이스입니다.
 */
public interface QueryActivityMinutesPort {
    /**
     * 기간 내 요일별 누적 세션 시간(분)을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 요일별 누적 세션 시간 맵
     */
    Map<LocalDate, Integer> findSessionMinutesInPeriod(Long userId, LocalDateTime start, LocalDateTime end);
}
