package whispy_server.whispy.domain.statistics.focus.daily.application.port.out;

import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.DailyFocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.DailyTagFocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.HourlyFocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.HourlyTagFocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.MonthlyFocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.daily.adapter.out.dto.MonthlyTagFocusAggregationDto;
import whispy_server.whispy.domain.statistics.focus.summary.adapter.out.dto.TagMinutesDto;
import whispy_server.whispy.domain.statistics.shared.adapter.out.dto.focus.FocusSessionDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 집중 통계 조회 포트.
 *
 * 집중 통계 조회를 위한 아웃바운드 포트 인터페이스입니다.
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
     * 시간별 집중 시간을 집계합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 시간별 집중 집계 DTO 목록
     */
    List<HourlyFocusAggregationDto> aggregateHourlyMinutes(Long userId, LocalDateTime start, LocalDateTime end);

    /**
     * 일별 집중 시간을 집계합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 일별 집중 집계 DTO 목록
     */
    List<DailyFocusAggregationDto> aggregateDailyMinutes(Long userId, LocalDateTime start, LocalDateTime end);

    /**
     * 월별 집중 시간을 집계합니다.
     *
     * @param userId 사용자 ID
     * @param year 연도
     * @return 월별 집중 집계 DTO 목록
     */
    List<MonthlyFocusAggregationDto> aggregateMonthlyMinutes(Long userId, int year);

    /**
     * 기간 내 총 집중 시간(분)을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 총 집중 시간(분)
     */
    int getTotalMinutes(Long userId, LocalDateTime start, LocalDateTime end);

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
     * 시간 및 태그별 집중 시간을 집계합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 시간 및 태그별 집중 집계 DTO 목록
     */
    List<HourlyTagFocusAggregationDto> aggregateHourlyByTag(Long userId, LocalDateTime start, LocalDateTime end);

    /**
     * 일 및 태그별 집중 시간을 집계합니다.
     *
     * @param userId 사용자 ID
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return 일 및 태그별 집중 집계 DTO 목록
     */
    List<DailyTagFocusAggregationDto> aggregateDailyByTag(Long userId, LocalDateTime start, LocalDateTime end);

    /**
     * 월 및 태그별 집중 시간을 집계합니다.
     *
     * @param userId 사용자 ID
     * @param year 연도
     * @return 월 및 태그별 집중 집계 DTO 목록
     */
    List<MonthlyTagFocusAggregationDto> aggregateMonthlyByTag(Long userId, int year);
}
