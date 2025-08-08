package whispy_server.whispy.domain.fcm.application.port.out;

import whispy_server.whispy.domain.fcm.model.TopicSubscription;
import whispy_server.whispy.domain.fcm.model.types.NotificationTopic;

import java.util.List;
import java.util.Optional;

public interface QueryTopicSubscriptionPort {
    List<TopicSubscription> findByEmail(String email);
    Optional<TopicSubscription> findByEmailAndTopic(String email, NotificationTopic topic);
    List<TopicSubscription> findSubscribedUserByTopic(NotificationTopic topic);
}
