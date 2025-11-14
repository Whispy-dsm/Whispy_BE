package whispy_server.whispy.domain.topic.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.topic.application.port.in.InitializeTopicsUseCase;
import whispy_server.whispy.domain.topic.application.port.out.QueryTopicSubscriptionPort;
import whispy_server.whispy.domain.topic.application.port.out.SaveTopicSubscriptionPort;
import whispy_server.whispy.domain.topic.model.TopicSubscription;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InitializeTopicsService implements InitializeTopicsUseCase {

    private final QueryTopicSubscriptionPort queryTopicSubscriptionPort;
    private final SaveTopicSubscriptionPort saveTopicSubscriptionPort;
    private final FcmSendPort fcmSendPort;

    @Override
    public void execute(String email, String fcmToken, boolean isEventAgreed) {
        executeForUser(email, fcmToken, isEventAgreed);

    }

    private void executeForUser(String email, String fcmToken, boolean isEventAgreed) {
        List<TopicSubscription> existingSubscriptions = queryTopicSubscriptionPort.findByEmail(email);

        if (existingSubscriptions.isEmpty()) {
            createAllTopicsForNewUser(email, fcmToken, isEventAgreed);
        } else {
            reRegisterFcmTokenForExistingUser(existingSubscriptions, fcmToken);
        }
    }

    private void createAllTopicsForNewUser(String email, String fcmToken, boolean isEventAgreed) {
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

                    if (isSubscribed && fcmToken != null) {
                        fcmSendPort.subscribeToTopic(fcmToken, topic);
                    }
                });
    }

    private void reRegisterFcmTokenForExistingUser(List<TopicSubscription> subscriptions, String fcmToken) {
        subscriptions.forEach(subscription -> {
            if (subscription.subscribed() && fcmToken != null) {
                fcmSendPort.subscribeToTopic(fcmToken, subscription.topic());
            }
        });
    }
}