package whispy_server.whispy.domain.statistics.sleep.daily.application.port.out;

import whispy_server.whispy.domain.statistics.sleep.daily.adapter.out.dto.DailySleepAggregationDto;
import whispy_server.whispy.domain.statistics.sleep.daily.adapter.out.dto.MonthlySleepAggregationDto;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.sleep.SleepSessionDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 수면 통계 조회 포트(일일).
 *
 * 수면 통계 일일 조회를 위한 아웃바운드 포트 인터페이스입니다.
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
     * 일별 수면 시간을 집계합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 일별 수면 집계 DTO 목록
     */
    List<DailySleepAggregationDto> aggregateDailyMinutes(Long userId, LocalDateTime start, LocalDateTime end);

    /**
     * 월별 수면 시간을 집계합니다.
     *
     * @param userId 사용자 ID
     * @param year 연도
     * @return 월별 수면 집계 DTO 목록
     */
    List<MonthlySleepAggregationDto> aggregateMonthlyMinutes(Long userId, int year);
}
