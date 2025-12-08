package whispy_server.whispy.domain.statistics.focus.summary.application.port.in;

import whispy_server.whispy.domain.statistics.focus.summary.adapter.in.web.dto.response.FocusStatisticsResponse;
import whispy_server.whispy.domain.statistics.focus.types.FocusPeriodType;
import whispy_server.whispy.global.annotation.UseCase;

import java.time.LocalDate;

/**
 * 집중 통계 조회 유스케이스.
 *
 * 사용자의 집중 통계를 조회하는 인바운드 포트입니다.
 */
@UseCase
public interface GetFocusStatisticsUseCase {
    /**
     * 집중 통계를 조회합니다.
     *
     * @param period 통계 기간 타입
     * @param date 기준 날짜
     * @return 집중 통계 응답
     */
    FocusStatisticsResponse execute(FocusPeriodType period, LocalDate date);
}
