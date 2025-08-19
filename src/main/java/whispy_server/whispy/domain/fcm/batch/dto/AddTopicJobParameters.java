package whispy_server.whispy.domain.fcm.batch.dto;

import whispy_server.whispy.domain.fcm.model.types.NotificationTopic;

public record AddTopicJobParameters(
        String email,
        String fcmToken,
        NotificationTopic topic,
        boolean defaultSubscribed
) {}
