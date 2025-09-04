package whispy_server.whispy.domain.topic.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.TopicSubscriptionRequest;
import whispy_server.whispy.domain.topic.application.port.in.UnSubscribeTopicUseCase;
import whispy_server.whispy.domain.topic.application.port.out.QueryTopicSubscriptionPort;
import whispy_server.whispy.domain.topic.application.port.out.SaveTopicSubscriptionPort;
import whispy_server.whispy.domain.topic.model.TopicSubscription;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.exception.domain.fcm.TopicSubscriptionNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class UnSubscribeTopicService implements UnSubscribeTopicUseCase {

    private final QueryTopicSubscriptionPort queryTopicSubscriptionPort;
    private final SaveTopicSubscriptionPort saveTopicSubscriptionPort;
    private final FcmSendPort fcmSendPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public void execute(TopicSubscriptionRequest request){
        User currentUser = userFacadeUseCase.currentUser();

        TopicSubscription subscription = queryTopicSubscriptionPort.findByEmailAndTopic(
                currentUser.email(), request.topic()
        ).orElseThrow(() -> TopicSubscriptionNotFoundException.EXCEPTION);

        TopicSubscription updatedSubscription = subscription.updateSubscription(false);
        saveTopicSubscriptionPort.save(updatedSubscription);

        fcmSendPort.unsubscribeFromTopic(currentUser.fcmToken(),  request.topic());
    }
}
