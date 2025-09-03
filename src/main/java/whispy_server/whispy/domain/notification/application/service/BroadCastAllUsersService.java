package whispy_server.whispy.domain.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationTopicSendRequest;
import whispy_server.whispy.domain.notification.application.port.in.BroadCastToAllUsersUseCase;
import whispy_server.whispy.domain.notification.application.port.in.SendToTopicUseCase;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

@Service
@RequiredArgsConstructor
public class BroadCastAllUsersService implements BroadCastToAllUsersUseCase {

    private final SendToTopicUseCase sendToTopicUseCase;

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
