package whispy_server.whispy.domain.statistics.focus.daily.application.port.in;

import whispy_server.whispy.domain.statistics.focus.daily.adapter.in.web.dto.response.DailyFocusStatisticsResponse;
import whispy_server.whispy.domain.statistics.focus.types.FocusPeriodType;
import whispy_server.whispy.global.annotation.UseCase;
import java.time.LocalDate;

/**
 * 일일 집중 통계 조회 유스케이스.
 *
 * 사용자의 집중 통계를 상세하게 조회하는 인바운드 포트입니다.
 */
@UseCase
public interface GetDailyFocusStatisticsUseCase {
    /**
     * 일일 집중 통계를 조회합니다.
     *
     * @param period 통계 기간 타입
     * @param date 기준 날짜
     * @return 일일 집중 통계 응답
     */
    DailyFocusStatisticsResponse execute(FocusPeriodType period, LocalDate date);
}
