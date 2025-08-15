package whispy_server.whispy.domain.fcm.application.port.out;

import whispy_server.whispy.domain.fcm.model.TopicSubscription;

public interface SaveTopicSubscriptionPort {
    TopicSubscription save(TopicSubscription topicSubscription);
}
