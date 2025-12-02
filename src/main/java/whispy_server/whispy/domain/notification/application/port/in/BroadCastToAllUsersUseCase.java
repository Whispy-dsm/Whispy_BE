package whispy_server.whispy.domain.notification.application.port.in;

import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationTopicSendRequest;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 모든 사용자에게 알림 전송 유스케이스.
 *
 * FCM을 통해 모든 사용자에게 푸시 알림을 전송하는 인바운드 포트입니다.
 */
@UseCase
public interface BroadCastToAllUsersUseCase {
    /**
     * 모든 사용자에게 알림을 전송합니다.
     *
     * @param request 알림 전송 요청
     */
    void execute(NotificationTopicSendRequest request);
}
