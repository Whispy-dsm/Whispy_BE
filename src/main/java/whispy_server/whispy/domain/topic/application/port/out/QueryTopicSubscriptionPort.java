package whispy_server.whispy.domain.topic.application.port.out;

import whispy_server.whispy.domain.topic.model.TopicSubscription;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import java.util.List;
import java.util.Optional;

public interface QueryTopicSubscriptionPort {
    List<TopicSubscription> findByEmail(String email);
    Optional<TopicSubscription> findByEmailAndTopic(String email, NotificationTopic topic);
    List<TopicSubscription> findSubscribedUserByTopic(NotificationTopic topic);
}
