package whispy_server.whispy.domain.statistics.sleep.comparison.application.port.in;

import whispy_server.whispy.domain.statistics.sleep.comparison.adapter.in.web.dto.response.SleepPeriodComparisonResponse;
import whispy_server.whispy.domain.statistics.sleep.types.SleepPeriodType;
import whispy_server.whispy.global.annotation.UseCase;

import java.time.LocalDate;

/**
 * 수면 기간 비교 조회 유스케이스.
 *
 * 사용자의 수면 통계를 기간별로 비교하는 인바운드 포트입니다.
 */
@UseCase
public interface GetSleepPeriodComparisonUseCase {
    /**
     * 수면 기간 비교 통계를 조회합니다.
     *
     * @param period 통계 기간 타입
     * @param date 기준 날짜
     * @return 수면 기간 비교 응답
     */
    SleepPeriodComparisonResponse execute(SleepPeriodType period, LocalDate date);
}
