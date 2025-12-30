package whispy_server.whispy.domain.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationTopicSendRequest;
import whispy_server.whispy.domain.notification.application.port.in.BroadCastToAllUsersUseCase;
import whispy_server.whispy.domain.notification.application.port.in.SendToTopicUseCase;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.global.annotation.AdminAction;

/**
 * 모든 사용자에게 알림 전송 서비스.
 *
 * 모든 사용자에게 FCM 푸시 알림을 브로드캐스트하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class BroadCastAllUsersService implements BroadCastToAllUsersUseCase {

    private final SendToTopicUseCase sendToTopicUseCase;

    /**
     * 모든 사용자에게 알림을 전송합니다.
     *
     * @param request 알림 토픽 전송 요청
     */
    @AdminAction("모든 사용자에게 알림 전송")
    @Override
    public void execute(NotificationTopicSendRequest request){

        NotificationTopicSendRequest notificationTopicSendrequest = new NotificationTopicSendRequest(
                NotificationTopic.BROADCAST_ANNOUNCEMENT,
                request.title(),
                request.body(),
                request.data()
        );

        sendToTopicUseCase.execute(notificationTopicSendrequest);
    }
}
