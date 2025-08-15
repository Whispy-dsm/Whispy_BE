package whispy_server.whispy.domain.fcm.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.fcm.adapter.in.web.dto.request.FcmSendRequest;
import whispy_server.whispy.domain.fcm.application.port.in.BroadCastToAllUsersUseCase;
import whispy_server.whispy.domain.fcm.application.port.in.SendToTopicUseCase;
import whispy_server.whispy.domain.fcm.model.types.NotificationTopic;

@Service
@RequiredArgsConstructor
public class BroadCastAllUsersService implements BroadCastToAllUsersUseCase {

    private final SendToTopicUseCase sendToTopicUseCase;

    @Override
    public void execute(FcmSendRequest request){

        FcmSendRequest fcmSendRequest = new FcmSendRequest(
                request.deviceTokens(),
                NotificationTopic.GENERAL_ANNOUNCEMENT,
                request.title(),
                request.body(),
                request.data()
        );

        sendToTopicUseCase.execute(fcmSendRequest);
    }
}
