package whispy_server.whispy.domain.notification.adapter.in.web.dto.request;

import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import java.util.List;
import java.util.Map;

public record NotificationSendRequest(
        String email,
        List<String> deviceTokens,
        NotificationTopic topic,
        String title,
        String body,
        Map<String, String> data
) {}
