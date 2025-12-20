package whispy_server.whispy.domain.topic.application.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.topic.application.port.out.QueryTopicSubscriptionPort;
import whispy_server.whispy.domain.topic.application.port.out.SaveTopicSubscriptionPort;
import whispy_server.whispy.domain.topic.model.TopicSubscription;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.global.exception.domain.fcm.TopicSubscriptionNotFoundException;

/**
 * 토픽 구독 활성화 컴포넌트.
 *
 * 토픽 구독 상태를 활성화하는 역할을 담당합니다.
 */
@Component
@RequiredArgsConstructor
public class TopicSubscriber {

    private final QueryTopicSubscriptionPort queryTopicSubscriptionPort;
    private final SaveTopicSubscriptionPort saveTopicSubscriptionPort;

    /**
     * 토픽 구독을 활성화합니다.
     *
     * @param email 사용자 이메일
     * @param topic 토픽
     * @throws TopicSubscriptionNotFoundException 토픽 구독이 존재하지 않는 경우
     */
    @Transactional
    public void subscribe(String email, NotificationTopic topic) {
        TopicSubscription subscription = queryTopicSubscriptionPort.findByEmailAndTopic(email, topic)
                .orElseThrow(() -> TopicSubscriptionNotFoundException.EXCEPTION);

        TopicSubscription updatedSubscription = subscription.updateSubscription(true);
        saveTopicSubscriptionPort.save(updatedSubscription);
    }
}
