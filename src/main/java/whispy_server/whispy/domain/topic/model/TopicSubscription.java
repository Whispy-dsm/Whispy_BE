package whispy_server.whispy.domain.topic.model;

import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.global.annotation.Aggregate;

@Aggregate
public record TopicSubscription(
        Long id,
        String email,
        NotificationTopic topic,
        boolean subscribed

) {
    public TopicSubscription updateSubscription(boolean isSubscribed) {
        return new TopicSubscription(
                this.id,
                this.email,
                this.topic,
                subscribed
        );
    }
}
