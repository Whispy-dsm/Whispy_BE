package whispy_server.whispy.domain.statistics.activity.applicatoin.port.out;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 주간 세션 존재 여부 조회 포트.
 *
 * 주간 세션 존재 여부 확인을 위한 아웃바운드 포트 인터페이스입니다.
 */
public interface CheckWeeklySessionExistsPort {
    /**
     * 기간 내 세션이 있는 날짜들을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 기간 내 세션이 있는 날짜 집합
     */
    Set<LocalDate> findSessionDatesInPeriod(Long userId, LocalDateTime start, LocalDateTime end);
}
