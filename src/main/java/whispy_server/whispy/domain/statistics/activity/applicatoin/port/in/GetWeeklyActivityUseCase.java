package whispy_server.whispy.domain.statistics.activity.applicatoin.port.in;

import whispy_server.whispy.domain.statistics.activity.adapter.in.web.dto.response.WeeklyActivityResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 주간 활동 통계 조회 유스케이스.
 *
 * 사용자의 주간 활동 통계를 조회하는 인바운드 포트입니다.
 */
@UseCase
public interface GetWeeklyActivityUseCase {
    /**
     * 주간 활동 통계를 조회합니다.
     *
     * @return 주간 활동 통계 응답
     */
    WeeklyActivityResponse execute();
}
