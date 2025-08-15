package whispy_server.whispy.domain.fcm.adapter.in.web.dto.request;

import whispy_server.whispy.domain.fcm.model.types.NotificationTopic;

import java.util.List;
import java.util.Map;

public record FcmSendRequest(
        List<String> deviceTokens,
        NotificationTopic topic,
        String title,
        String body,
        Map<String, String> data
) {}
