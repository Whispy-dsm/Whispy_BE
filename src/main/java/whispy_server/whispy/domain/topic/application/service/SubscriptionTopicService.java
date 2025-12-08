package whispy_server.whispy.domain.topic.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.TopicSubscriptionRequest;
import whispy_server.whispy.domain.topic.application.port.in.SubscriptionTopicUseCase;
import whispy_server.whispy.domain.topic.application.port.out.QueryTopicSubscriptionPort;
import whispy_server.whispy.domain.topic.application.port.out.SaveTopicSubscriptionPort;
import whispy_server.whispy.domain.topic.model.TopicSubscription;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.exception.domain.fcm.TopicSubscriptionNotFoundException;

/**
 * 토픽 구독 서비스.
 *
 * 사용자의 토픽 구독을 처리하는 서비스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionTopicService implements SubscriptionTopicUseCase {

    private final QueryTopicSubscriptionPort queryTopicSubscriptionPort;
    private final SaveTopicSubscriptionPort saveTopicSubscriptionPort;
    private final FcmSendPort fcmSendPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 토픽을 구독합니다.
     *
     * @param request 토픽 구독 요청
     * @throws TopicSubscriptionNotFoundException 토픽 구독이 존재하지 않는 경우
     */
    @Override
    public void execute(TopicSubscriptionRequest request){
        User currentUser = userFacadeUseCase.currentUser();

        TopicSubscription subscription = queryTopicSubscriptionPort.findByEmailAndTopic(
                currentUser.email(), request.topic()
        ).orElseThrow(() -> TopicSubscriptionNotFoundException.EXCEPTION);

        TopicSubscription updateSubscription = subscription.updateSubscription(true);
        saveTopicSubscriptionPort.save(updateSubscription);

        fcmSendPort.subscribeToTopic(currentUser.fcmToken(), request.topic());
    }
}
