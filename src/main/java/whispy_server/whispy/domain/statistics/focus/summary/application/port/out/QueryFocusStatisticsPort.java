package whispy_server.whispy.domain.statistics.focus.summary.application.port.out;

import whispy_server.whispy.domain.statistics.focus.summary.adapter.out.dto.FocusAggregationDto;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.focus.FocusSessionDto;
import whispy_server.whispy.domain.statistics.focus.summary.adapter.out.dto.TagMinutesDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 집중 통계 조회 포트(요약).
 *
 * 집중 통계 요약 조회를 위한 아웃바운드 포트 인터페이스입니다.
 */
public interface QueryFocusStatisticsPort {
    /**
     * 기간 내 사용자의 집중 세션을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 집중 세션 DTO 목록
     */
    List<FocusSessionDto> findByUserIdAndPeriod(Long userId, LocalDateTime start, LocalDateTime end);

    /**
     * 기간별로 집중 통계를 집계합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 집중 집계 DTO
     */
    FocusAggregationDto aggregateByPeriod(Long userId, LocalDateTime start, LocalDateTime end);

    /**
     * 특정 날짜의 누적 집중 시간(분)을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param date 조회 날짜
     * @return 누적 집중 시간(분)
     */
    int sumMinutesByDate(Long userId, LocalDate date);

    /**
     * 태그별 집중 시간을 집계합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 태그별 시간 DTO 목록
     */
    List<TagMinutesDto> aggregateByTag(Long userId, LocalDateTime start, LocalDateTime end);

    /**
     * 기간 내 집중 세션이 있는 일수를 계산합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 집중 세션이 있는 일수
     */
    int countDistinctDays(Long userId, LocalDateTime start, LocalDateTime end);
}
