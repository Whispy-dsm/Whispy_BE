package whispy_server.whispy.domain.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.FcmSendRequest;
import whispy_server.whispy.domain.notification.application.port.in.SendToDeviceTokensUseCase;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;

@Service
@RequiredArgsConstructor
public class SendToDeviceTokensService implements SendToDeviceTokensUseCase {

    private final FcmSendPort fcmSendPort;

    @Override
    public void execute(FcmSendRequest request){
        fcmSendPort.sendMulticast(
                request.deviceTokens(),
                request.title(),
                request.body(),
                request.data()
        );
    }
}
