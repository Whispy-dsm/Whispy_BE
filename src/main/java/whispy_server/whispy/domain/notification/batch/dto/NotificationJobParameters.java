package whispy_server.whispy.domain.notification.batch.dto;

import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import java.util.Map;

public record NotificationJobParameters(
        String email,
        String title,
        String body,
        NotificationTopic topic,
        Map<String, String> data
) {}
