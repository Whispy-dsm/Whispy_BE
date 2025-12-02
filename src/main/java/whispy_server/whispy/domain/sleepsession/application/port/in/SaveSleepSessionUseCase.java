package whispy_server.whispy.domain.sleepsession.application.port.in;

import whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.request.SaveSleepSessionRequest;
import whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response.SleepSessionResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 수면 세션 저장 유스케이스 인터페이스.
 *
 * 새로운 수면 세션을 저장하는 애플리케이션 작업을 정의합니다.
 */
@UseCase
public interface SaveSleepSessionUseCase {

    /**
     * 새로운 수면 세션을 저장합니다.
     *
     * @param request 수면 세션 저장 요청 (시작 시간, 종료 시간, 지속 시간)
     * @return 저장된 세션 정보를 포함한 응답
     */
    SleepSessionResponse execute(SaveSleepSessionRequest request);
}
