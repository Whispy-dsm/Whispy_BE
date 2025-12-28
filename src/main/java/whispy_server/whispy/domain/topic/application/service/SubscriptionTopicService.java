package whispy_server.whispy.domain.topic.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.TopicSubscriptionRequest;
import whispy_server.whispy.domain.topic.application.port.in.SubscriptionTopicUseCase;
import whispy_server.whispy.domain.topic.application.service.component.TopicSubscriber;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.annotation.UserAction;

/**
 * 토픽 구독 서비스.
 *
 * 사용자의 토픽 구독을 처리하는 서비스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class SubscriptionTopicService implements SubscriptionTopicUseCase {

    private final FcmSendPort fcmSendPort;
    private final UserFacadeUseCase userFacadeUseCase;
    private final TopicSubscriber topicSubscriber;

    /**
     * 토픽을 구독합니다.
     *
     * @param request 토픽 구독 요청
     */
    @Override
    @UserAction("토픽 구독")
    public void execute(TopicSubscriptionRequest request){
        User currentUser = userFacadeUseCase.currentUser();

        // 트랜잭션 안에서 DB 저장
        topicSubscriber.subscribe(currentUser.email(), request.topic());

        // 트랜잭션 밖에서 FCM 구독
        fcmSendPort.subscribeToTopic(currentUser.fcmToken(), request.topic());
    }
}
