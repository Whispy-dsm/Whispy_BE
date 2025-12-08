package whispy_server.whispy.domain.notification.application.port.in;

import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationSendRequest;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 디바이스 토큰으로 알림 전송 유스케이스.
 *
 * FCM 디바이스 토큰을 사용하여 알림을 전송하는 인바운드 포트입니다.
 */
@UseCase
public interface SendToDeviceTokensUseCase {
    /**
     * 디바이스 토큰으로 알림을 전송합니다.
     *
     * @param request 알림 전송 요청
     */
    void execute(NotificationSendRequest request);
}
