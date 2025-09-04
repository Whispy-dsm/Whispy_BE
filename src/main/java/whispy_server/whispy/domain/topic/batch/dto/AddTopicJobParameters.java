package whispy_server.whispy.domain.topic.batch.dto;

import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

public record AddTopicJobParameters(
        String email,
        String fcmToken,
        NotificationTopic topic,
        boolean defaultSubscribed
) {}
