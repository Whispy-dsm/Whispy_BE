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
    public void execute(String email, String fcmToken) {
        executeForUser(email, fcmToken);

    }

    private void executeForUser(String email, String fcmToken) {
        List<TopicSubscription> existingSubscriptions = queryTopicSubscriptionPort.findByEmail(email);

        if (existingSubscriptions.isEmpty()) {
            createAllTopicsForNewUser(email, fcmToken);
        } else {
            reRegisterFcmTokenForExistingUser(existingSubscriptions, fcmToken);
        }
    }

    private void createAllTopicsForNewUser(String email, String fcmToken) {
        Arrays.stream(NotificationTopic.values())
                .forEach(topic -> {
                    boolean defaultSubscribed = (topic == NotificationTopic.GENERAL_ANNOUNCEMENT);

                    TopicSubscription subscription = new TopicSubscription(
                            null,
                            email,
                            topic,
                            defaultSubscribed
                    );
                    saveTopicSubscriptionPort.save(subscription);

                    if (defaultSubscribed && fcmToken != null) {
                        fcmSendPort.subscribeToTopic(fcmToken, topic);
                    }
                });
    }

    private void reRegisterFcmTokenForExistingUser(List<TopicSubscription> subscriptions, String fcmToken) {
        subscriptions.forEach(subscription -> {
            if (subscription.isSubscribed() && fcmToken != null) {
                fcmSendPort.subscribeToTopic(fcmToken, subscription.topic());
            }
        });
    }
}