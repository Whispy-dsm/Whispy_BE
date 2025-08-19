package whispy_server.whispy.domain.fcm.application.port.out;

import whispy_server.whispy.domain.fcm.model.TopicSubscription;

import java.util.List;

public interface SaveTopicSubscriptionPort {
    TopicSubscription save(TopicSubscription topicSubscription);
    List<TopicSubscription> saveAll(List<TopicSubscription> subscriptions);
}
