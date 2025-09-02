package whispy_server.whispy.domain.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.FcmSendRequest;
import whispy_server.whispy.domain.notification.application.port.in.SendToDeviceTokensUseCase;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.notification.application.port.out.SaveNotificationPort;
import whispy_server.whispy.domain.notification.model.Notification;

@Service
@RequiredArgsConstructor
public class SendToDeviceTokensService implements SendToDeviceTokensUseCase {

    private final FcmSendPort fcmSendPort;
    private final SaveNotificationPort saveNotificationPort;

    @Override
    public void execute(FcmSendRequest request){
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
                false
        );
        saveNotificationPort.save(notification);
    }
}
