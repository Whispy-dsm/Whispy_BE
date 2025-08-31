package whispy_server.whispy.domain.topic.adapter.in.web.dto.response;

import whispy_server.whispy.domain.topic.model.TopicSubscription;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import java.util.UUID;

public record TopicSubscriptionResponse(
        UUID id,
        String email,
        NotificationTopic topic,
        boolean isSubscribed
){

    public static TopicSubscriptionResponse from(TopicSubscription subscription) {
        return new TopicSubscriptionResponse(
                subscription.id(),
                subscription.email(),
                subscription.topic(),
                subscription.isSubscribed()
        );
    }
}
