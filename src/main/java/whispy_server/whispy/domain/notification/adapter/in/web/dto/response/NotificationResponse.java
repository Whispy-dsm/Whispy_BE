package whispy_server.whispy.domain.notification.adapter.in.web.dto.response;

import whispy_server.whispy.domain.notification.model.Notification;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import java.util.Map;

public record NotificationResponse(
        Long id,
        String email,
        String title,
        String body,
        NotificationTopic topic,
        Map<String, String> data,
        boolean read
) {

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.id(),
                notification.email(),
                notification.title(),
                notification.body(),
                notification.topic(),
                notification.data(),
                notification.read()
        );
    }
}
