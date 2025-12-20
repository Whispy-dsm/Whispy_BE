package whispy_server.whispy.domain.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationTopicSendRequest;
import whispy_server.whispy.domain.notification.application.port.in.SendToTopicUseCase;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.notification.batch.trigger.SaveNotificationBatchTrigger;

/**
 * 토픽으로 알림 전송 서비스.
 *
 * FCM 토픽을 통해 알림을 전송하고 배치 작업을 트리거하여 알림 이력을 저장하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class SendToTopicBatchService implements SendToTopicUseCase {

    private final FcmSendPort fcmSendPort;
    private final SaveNotificationBatchTrigger saveNotificationBatchTrigger;

    /**
     * 토픽으로 알림을 전송하고 배치 작업을 통해 알림 이력을 저장합니다.
     *
     * @param request 알림 토픽 전송 요청
     */
    @Override
    public void execute(NotificationTopicSendRequest request) {

        fcmSendPort.sendToTopic(
                request.topic(),
                request.title(),
                request.body(),
                request.data()
        );
        saveNotificationBatchTrigger.trigger(request);
    }
}
