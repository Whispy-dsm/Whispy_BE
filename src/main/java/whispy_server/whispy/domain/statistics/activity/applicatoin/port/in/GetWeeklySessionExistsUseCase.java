package whispy_server.whispy.domain.statistics.activity.applicatoin.port.in;

import whispy_server.whispy.domain.statistics.activity.adapter.in.web.dto.response.WeeklySessionExistsResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 주간 세션 존재 여부 조회 유스케이스.
 *
 * 사용자의 요일별 세션 존재 여부를 조회하는 인바운드 포트입니다.
 */
@UseCase
public interface GetWeeklySessionExistsUseCase {
    /**
     * 요일별 세션 존재 여부를 조회합니다.
     *
     * @return 주간 세션 존재 여부 응답
     */
    WeeklySessionExistsResponse execute();
}
