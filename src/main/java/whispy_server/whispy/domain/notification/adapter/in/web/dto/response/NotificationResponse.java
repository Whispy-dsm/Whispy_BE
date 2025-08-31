package whispy_server.whispy.domain.notification.adapter.in.web.dto.response;

import whispy_server.whispy.domain.notification.model.Notification;
import java.util.Map;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        String email,
        String title,
        String body,
        whispy_server.whispy.domain.topic.model.types.NotificationTopic topic,
        Map<String, String> data,
        boolean isRead
) {

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.id(),
                notification.email(),
                notification.title(),
                notification.body(),
                notification.topic(),
                notification.data(),
                notification.isRead()
        );
    }
}
