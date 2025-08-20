package whispy_server.whispy.domain.fcm.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.fcm.application.port.in.InitializeTopicsUseCase;
import whispy_server.whispy.domain.fcm.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.fcm.application.port.out.QueryTopicSubscriptionPort;
import whispy_server.whispy.domain.fcm.application.port.out.SaveTopicSubscriptionPort;
import whispy_server.whispy.domain.fcm.model.TopicSubscription;
import whispy_server.whispy.domain.fcm.model.types.NotificationTopic;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InitializeTopicsService implements InitializeTopicsUseCase {

    private final QueryTopicSubscriptionPort queryTopicSubscriptionPort;
    private final SaveTopicSubscriptionPort saveTopicSubscriptionPort;
    private final FcmSendPort fcmSendPort;
    private final UserFacadeUseCase userFacadeUseCase;

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