package whispy_server.whispy.domain.sleepsession.application.port.in;

import whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response.SleepSessionDetailResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 수면 세션 상세 조회 유스케이스 인터페이스.
 *
 * 특정 수면 세션의 상세 정보를 조회하는 애플리케이션 작업을 정의합니다.
 */
@UseCase
public interface GetSleepSessionDetailUseCase {

    /**
     * 특정 수면 세션의 상세 정보를 조회합니다.
     *
     * @param sleepSessionId 조회할 수면 세션 ID
     * @return 수면 세션 상세 정보
     * @throws SleepSessionNotFoundException 해당 ID의 세션이 존재하지 않을 경우
     */
    SleepSessionDetailResponse execute(Long sleepSessionId);
}
