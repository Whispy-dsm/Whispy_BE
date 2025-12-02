package whispy_server.whispy.domain.focussession.application.port.in;

import whispy_server.whispy.domain.focussession.adapter.in.web.dto.request.SaveFocusSessionRequest;
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.response.FocusSessionResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 집중 세션 저장 유스케이스 인터페이스.
 *
 * 새로운 집중 세션을 저장하는 애플리케이션 작업을 정의합니다.
 */
@UseCase
public interface SaveFocusSessionUseCase {

    /**
     * 새로운 집중 세션을 저장합니다.
     *
     * @param request 집중 세션 저장 요청 (시작 시간, 종료 시간, 지속 시간, 태그)
     * @return 저장된 세션 정보를 포함한 응답
     */
    FocusSessionResponse execute(SaveFocusSessionRequest request);
}
