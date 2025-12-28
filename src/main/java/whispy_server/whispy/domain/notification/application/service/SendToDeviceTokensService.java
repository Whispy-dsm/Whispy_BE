package whispy_server.whispy.domain.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationSendRequest;
import whispy_server.whispy.domain.notification.application.port.in.SendToDeviceTokensUseCase;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.notification.application.service.component.NotificationPersister;
import whispy_server.whispy.domain.notification.model.Notification;
import whispy_server.whispy.global.annotation.UserAction;

/**
 * 디바이스 토큰으로 알림 전송 서비스.
 *
 * 특정 디바이스 토큰 목록으로 FCM 푸시 알림을 전송하고 알림 이력을 저장하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class SendToDeviceTokensService implements SendToDeviceTokensUseCase {

    private final FcmSendPort fcmSendPort;
    private final NotificationPersister notificationPersister;

    /**
     * 디바이스 토큰으로 알림을 전송합니다.
     *
     * @param request 알림 전송 요청
     */
    @UserAction("디바이스 토큰으로 알림 전송")
    @Override
    public void execute(NotificationSendRequest request){
        // 1. 외부 API 호출 (실패 시 여기서 예외 발생 -> 저장 로직 실행 안 됨)
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

        notificationPersister.save(notification);
    }
}
