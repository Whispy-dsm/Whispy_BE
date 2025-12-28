package whispy_server.whispy.domain.topic.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.TopicSubscriptionRequest;
import whispy_server.whispy.domain.topic.application.port.in.UnSubscribeTopicUseCase;
import whispy_server.whispy.domain.topic.application.service.component.TopicUnsubscriber;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.annotation.UserAction;

/**
 * 토픽 구독 취소 서비스.
 *
 * 사용자의 토픽 구독 취소를 처리하는 서비스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class UnSubscribeTopicService implements UnSubscribeTopicUseCase {

    private final FcmSendPort fcmSendPort;
    private final UserFacadeUseCase userFacadeUseCase;
    private final TopicUnsubscriber topicUnsubscriber;

    /**
     * 토픽 구독을 취소합니다.
     *
     * @param request 토픽 구독 취소 요청
     */
    @Override
    @UserAction("토픽 구독 취소")
    public void execute(TopicSubscriptionRequest request){
        User currentUser = userFacadeUseCase.currentUser();

        // 트랜잭션 안에서 DB 저장
        topicUnsubscriber.unsubscribe(currentUser.email(), request.topic());

        // 트랜잭션 밖에서 FCM 구독 취소
        fcmSendPort.unsubscribeFromTopic(currentUser.fcmToken(), request.topic());
    }
}
