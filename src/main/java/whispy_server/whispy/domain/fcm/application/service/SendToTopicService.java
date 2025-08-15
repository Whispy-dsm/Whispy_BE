package whispy_server.whispy.domain.fcm.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.fcm.adapter.in.web.dto.request.FcmSendRequest;
import whispy_server.whispy.domain.fcm.application.port.in.SendToTopicUseCase;
import whispy_server.whispy.domain.fcm.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.fcm.application.port.out.QueryTopicSubscriptionPort;
import whispy_server.whispy.domain.fcm.application.port.out.SaveNotificationPort;
import whispy_server.whispy.domain.fcm.model.Notification;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class SendToTopicService implements SendToTopicUseCase {

    private final FcmSendPort fcmSendPort;
    private final SaveNotificationPort saveNotificationPort;
    private final QueryTopicSubscriptionPort queryTopicSubscriptionPort;

    @Override
    public void execute(FcmSendRequest request){
        fcmSendPort.sendToTopic(
                request.topic(),
                request.title(),
                request.body(),
                request.data()
        );

        var subscribers = queryTopicSubscriptionPort.findSubscribedUserByTopic(request.topic());

        subscribers.forEach(subscription -> {
            Notification notification = new Notification(
                    null,
                    subscription.email(),
                    request.title(),
                    request.body(),
                    request.topic(),
                    request.data() != null ? request.data() : Collections.emptyMap(),
                    false
            );
            saveNotificationPort.save(notification);
        });
    }
    

}
