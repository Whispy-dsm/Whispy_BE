package whispy_server.whispy.domain.topic.application.port.in;

import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.InitializeTopicsRequest;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 토픽 초기화 유스케이스.
 *
 * 신규 사용자의 토픽을 초기화하거나 기존 사용자의 FCM 토큰을 재등록합니다.
 */
@UseCase
public interface InitializeTopicsUseCase {
    /**
     * 사용자의 토픽을 초기화합니다.
     *
     * @param request 토픽 초기화 요청 (이메일, FCM 토큰, 이벤트 수신 동의 여부)
     */
    void execute(InitializeTopicsRequest request);
}
