package whispy_server.whispy.domain.fcm.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.fcm.adapter.in.web.dto.request.FcmSendRequest;
import whispy_server.whispy.domain.fcm.application.port.in.SendToDeviceTokensUseCase;
import whispy_server.whispy.domain.fcm.application.port.out.FcmSendPort;

import java.util.Collections;

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
