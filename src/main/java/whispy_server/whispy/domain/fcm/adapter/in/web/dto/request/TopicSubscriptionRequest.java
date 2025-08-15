package whispy_server.whispy.domain.fcm.adapter.in.web.dto.request;

import whispy_server.whispy.domain.fcm.model.types.NotificationTopic;

public record TopicSubscriptionRequest(
        NotificationTopic topic
) {}
