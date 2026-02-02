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
     * 로직:
     * 1. 기존 구독 정보가 없으면 → 신규 사용자로 판단, 모든 토픽 생성
     * 2. 기존 구독 정보가 있으면 → FCM 토큰만 재등록 (기기 변경)
     *
     * @param email         사용자 이메일
     * @param fcmToken      FCM 토큰
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
     * 토픽 구독 규칙:
     * - ONLY_ADMIN: 구독 안 함 (관리자 전용)
     * - GENERAL_ANNOUNCEMENT: 이벤트 수신 동의 여부에 따름
     * - 나머지 토픽: 모두 구독 (SYSTEM_ANNOUNCEMENT, SUBSCRIPTION_NOTICE 등)
     *
     * 프로세스:
     * 1. 트랜잭션 안에서 DB에 토픽 구독 정보 저장
     * 2. 트랜잭션 밖에서 Firebase에 FCM 토큰 구독
     *
     * @param email         사용자 이메일
     * @param fcmToken      FCM 토큰
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
     * 사용자가 새 기기에서 로그인했을 때, 기존 토픽 구독 정보를 바탕으로
     * 새 FCM 토큰을 Firebase에 재구독합니다.
     *
     * 조건:
     * - subscription.subscribed() == true: 사용자가 해당 토픽을 구독 중인 경우
     * - fcmToken != null: FCM 토큰이 제공된 경우
     *
     * @param subscriptions 토픽 구독 목록
     * @param fcmToken      FCM 토큰
     */
    private void reRegisterFcmTokenForExistingUser(List<TopicSubscription> subscriptions, String fcmToken) {
        subscriptions.forEach(subscription -> {
            if (subscription.subscribed() && fcmToken != null) {
                fcmSendPort.subscribeToTopic(fcmToken, subscription.topic());
            }
        });
    }
}