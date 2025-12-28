package whispy_server.whispy.domain.user.application.port.in;

import whispy_server.whispy.domain.user.adapter.in.web.dto.request.UpdateFcmTokenRequest;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * FCM 토큰 업데이트 유스케이스.
 * 푸시 알림을 위한 Firebase Cloud Messaging 토큰을 업데이트합니다.
 */
@UseCase
public interface UpdateFcmTokenUseCase {
    /**
     * 현재 인증된 사용자의 FCM 토큰을 업데이트합니다.
     *
     * @param request FCM 토큰 업데이트 요청
     */
    void execute(UpdateFcmTokenRequest request);
}
