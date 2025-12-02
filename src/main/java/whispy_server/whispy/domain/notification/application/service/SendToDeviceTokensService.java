package whispy_server.whispy.domain.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationSendRequest;
import whispy_server.whispy.domain.notification.application.port.in.SendToDeviceTokensUseCase;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.notification.application.port.out.SaveNotificationPort;
import whispy_server.whispy.domain.notification.model.Notification;

/**
 * 디바이스 토큰으로 알림 전송 서비스.
 *
 * 특정 디바이스 토큰 목록으로 FCM 푸시 알림을 전송하고 알림 이력을 저장하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SendToDeviceTokensService implements SendToDeviceTokensUseCase {

    private final FcmSendPort fcmSendPort;
    private final SaveNotificationPort saveNotificationPort;

    /**
     * 디바이스 토큰으로 알림을 전송합니다.
     *
     * @param request 알림 전송 요청
     */
    @Override
    public void execute(NotificationSendRequest request){
        fcmSendPort.sendMulticast(
                request.deviceTokens(),
                request.title(),
                request.body(),
                request.data()
        );

        Notification notification = new Notification(
                null,
                request.email(),
                request.title(),
                request.body(),
                request.topic(),
                request.data(),
                false,
                null
        );
        saveNotificationPort.save(notification);
    }
}
