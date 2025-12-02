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

/**
 * 토픽 구독 취소 서비스.
 *
 * 사용자의 토픽 구독 취소를 처리하는 서비스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UnSubscribeTopicService implements UnSubscribeTopicUseCase {

    private final QueryTopicSubscriptionPort queryTopicSubscriptionPort;
    private final SaveTopicSubscriptionPort saveTopicSubscriptionPort;
    private final FcmSendPort fcmSendPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 토픽 구독을 취소합니다.
     *
     * @param request 토픽 구독 취소 요청
     * @throws whispy_server.whispy.global.exception.domain.fcm.TopicSubscriptionNotFoundException 토픽 구독이 존재하지 않는 경우
     */
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
