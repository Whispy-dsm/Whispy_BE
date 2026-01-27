package whispy_server.whispy.domain.topic.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.InitializeTopicsRequest;
import whispy_server.whispy.domain.topic.application.port.in.InitializeTopicsUseCase;
import whispy_server.whispy.domain.topic.application.port.out.QueryTopicSubscriptionPort;
import whispy_server.whispy.domain.topic.application.service.component.TopicInitializer;
import whispy_server.whispy.domain.topic.model.TopicSubscription;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.global.annotation.UserAction;

import java.util.Arrays;
import java.util.List;

/**
 * 토픽 초기화 서비스.
 *
 * 신규 사용자의 토픽을 초기화하거나 기존 사용자의 FCM 토큰을 재등록하는 서비스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class InitializeTopicsService implements InitializeTopicsUseCase {

    private final QueryTopicSubscriptionPort queryTopicSubscriptionPort;
    private final FcmSendPort fcmSendPort;
    private final TopicInitializer topicInitializer;

    /**
     * 사용자의 토픽을 초기화합니다.
     *
     * @param request 토픽 초기화 요청 (이메일, FCM 토큰, 이벤트 수신 동의 여부)
     */
    @UserAction("토픽 초기화")
    @Override
    public void execute(InitializeTopicsRequest request) {
        executeForUser(request.email(), request.fcmToken(), request.isEventAgreed());
    }

    /**
     * 사용자별 토픽 초기화를 수행합니다.
     *
     * @param email 사용자 이메일
     * @param fcmToken FCM 토큰
     * @param isEventAgreed 이벤트 수신 동의 여부
     */
    private void executeForUser(String email, String fcmToken, boolean isEventAgreed) {
        List<TopicSubscription> existingSubscriptions = queryTopicSubscriptionPort.findByEmail(email);

        if (existingSubscriptions.isEmpty()) {
            createAllTopicsForNewUser(email, fcmToken, isEventAgreed);
        } else {
            reRegisterFcmTokenForExistingUser(existingSubscriptions, fcmToken);
        }
    }

    /**
     * 신규 사용자를 위한 모든 토픽을 생성합니다.
     *
     * @param email 사용자 이메일
     * @param fcmToken FCM 토큰
     * @param isEventAgreed 이벤트 수신 동의 여부
     */
    private void createAllTopicsForNewUser(String email, String fcmToken, boolean isEventAgreed) {
        // 트랜잭션 안에서 DB 저장
        topicInitializer.createAllTopicsForNewUser(email, isEventAgreed);

        // 트랜잭션 밖에서 FCM 구독
        if (fcmToken != null) {
            Arrays.stream(NotificationTopic.values())
                    .forEach(topic -> {
                        boolean isSubscribed = switch (topic) {
                            case ONLY_ADMIN -> false;
                            case GENERAL_ANNOUNCEMENT -> isEventAgreed;
                            default -> true;
                        };

                        if (isSubscribed) {
                            fcmSendPort.subscribeToTopic(fcmToken, topic);
                        }
                    });
        }
    }

    /**
     * 기존 사용자의 FCM 토큰을 재등록합니다.
     *
     * @param subscriptions 토픽 구독 목록
     * @param fcmToken FCM 토큰
     */
    private void reRegisterFcmTokenForExistingUser(List<TopicSubscription> subscriptions, String fcmToken) {
        subscriptions.forEach(subscription -> {
            if (subscription.subscribed() && fcmToken != null) {
                fcmSendPort.subscribeToTopic(fcmToken, subscription.topic());
            }
        });
    }
}