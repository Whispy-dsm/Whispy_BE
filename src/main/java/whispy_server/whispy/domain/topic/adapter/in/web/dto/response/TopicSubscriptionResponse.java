package whispy_server.whispy.domain.topic.adapter.in.web.dto.response;

import whispy_server.whispy.domain.topic.model.TopicSubscription;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

public record TopicSubscriptionResponse(
        Long id,
        String email,
        NotificationTopic topic,
        boolean Subscribed
){

    public static TopicSubscriptionResponse from(TopicSubscription subscription) {
        return new TopicSubscriptionResponse(
                subscription.id(),
                subscription.email(),
                subscription.topic(),
                subscription.subscribed()
        );
    }
}
