package whispy_server.whispy.domain.topic.application.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.topic.application.port.out.SaveTopicSubscriptionPort;
import whispy_server.whispy.domain.topic.model.TopicSubscription;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import java.util.Arrays;

/**
 * 토픽 초기화 컴포넌트.
 *
 * 신규 사용자의 토픽 구독을 트랜잭션 내에서 생성하는 역할을 담당합니다.
 */
@Component
@RequiredArgsConstructor
public class TopicInitializer {

    private final SaveTopicSubscriptionPort saveTopicSubscriptionPort;

    /**
     * 신규 사용자를 위한 모든 토픽을 생성합니다.
     *
     * @param email 사용자 이메일
     * @param isEventAgreed 이벤트 수신 동의 여부
     */
    @Transactional
    public void createAllTopicsForNewUser(String email, boolean isEventAgreed) {
        Arrays.stream(NotificationTopic.values())
                .forEach(topic -> {
                    boolean isSubscribed = switch (topic) {
                        case ONLY_ADMIN -> false;
                        case GENERAL_ANNOUNCEMENT -> isEventAgreed;
                        default -> true;
                    };

                    TopicSubscription subscription = new TopicSubscription(
                            null,
                            email,
                            topic,
                            isSubscribed
                    );
                    saveTopicSubscriptionPort.save(subscription);
                });
    }
}
