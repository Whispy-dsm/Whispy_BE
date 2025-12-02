package whispy_server.whispy.domain.meditationsession.application.port.in;

import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.request.SaveMeditationSessionRequest;
import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response.MeditationSessionResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 명상 세션 저장 유스케이스 인터페이스.
 *
 * 새로운 명상 세션을 저장하는 애플리케이션 작업을 정의합니다.
 */
@UseCase
public interface SaveMeditationSessionUseCase {

    /**
     * 새로운 명상 세션을 저장합니다.
     *
     * @param request 명상 세션 저장 요청 (시작 시간, 종료 시간, 지속 시간, 호흡 모드)
     * @return 저장된 세션 정보를 포함한 응답
     */
    MeditationSessionResponse execute(SaveMeditationSessionRequest request);
}
