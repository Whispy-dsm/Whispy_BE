package whispy_server.whispy.domain.meditationsession.application.port.in;

import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response.MeditationSessionDetailResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 명상 세션 상세 조회 유스케이스 인터페이스.
 *
 * 특정 명상 세션의 상세 정보를 조회하는 애플리케이션 작업을 정의합니다.
 */
@UseCase
public interface GetMeditationSessionDetailUseCase {

    /**
     * 특정 명상 세션의 상세 정보를 조회합니다.
     *
     * @param meditationSessionId 조회할 명상 세션 ID
     * @return 명상 세션 상세 정보
     * @throws MeditationSessionNotFoundException 해당 ID의 세션이 존재하지 않을 경우
     */
    MeditationSessionDetailResponse execute(Long meditationSessionId);
}
