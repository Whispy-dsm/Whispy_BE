package whispy_server.whispy.domain.notification.application.port.in;

import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationTopicSendRequest;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 토픽으로 알림 전송 유스케이스.
 *
 * FCM 토픽을 사용하여 알림을 전송하는 인바운드 포트입니다.
 */
@UseCase
public interface SendToTopicUseCase {
    /**
     * 토픽으로 알림을 전송합니다.
     *
     * @param request 알림 토픽 전송 요청
     */
    void execute(NotificationTopicSendRequest request);
}
