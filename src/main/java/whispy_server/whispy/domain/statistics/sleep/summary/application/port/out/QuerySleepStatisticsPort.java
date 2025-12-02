package whispy_server.whispy.domain.statistics.sleep.summary.application.port.out;

import whispy_server.whispy.domain.statistics.sleep.summary.adapter.out.dto.SleepAggregationDto;
import whispy_server.whispy.domain.statistics.sleep.summary.adapter.out.dto.SleepDetailedAggregationDto;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.sleep.SleepSessionDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 수면 통계 조회 포트(요약).
 *
 * 수면 통계 요약 조회를 위한 아웃바운드 포트 인터페이스입니다.
 */
public interface QuerySleepStatisticsPort {
    /**
     * 기간 내 사용자의 수면 세션을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 수면 세션 DTO 목록
     */
    List<SleepSessionDto> findByUserIdAndPeriod(Long userId, LocalDateTime start, LocalDateTime end);

    /**
     * 기간별로 수면 통계를 집계합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 수면 집계 DTO
     */
    SleepAggregationDto aggregateByPeriod(Long userId, LocalDateTime start, LocalDateTime end);

    /**
     * 특정 날짜의 누적 수면 시간(분)을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param date 조회 날짜
     * @return 누적 수면 시간(분)
     */
    int sumMinutesByDate(Long userId, LocalDate date);

    /**
     * 상세한 수면 통계를 집계합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 상세 수면 집계 DTO
     */
    SleepDetailedAggregationDto aggregateDetailedStatistics(Long userId, LocalDateTime start, LocalDateTime end);
}
