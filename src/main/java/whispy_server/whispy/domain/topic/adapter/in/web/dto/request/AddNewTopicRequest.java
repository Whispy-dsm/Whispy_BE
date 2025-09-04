package whispy_server.whispy.domain.topic.adapter.in.web.dto.request;

import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

public record AddNewTopicRequest(
        NotificationTopic topic,
        boolean defaultSubscribed
) {}
