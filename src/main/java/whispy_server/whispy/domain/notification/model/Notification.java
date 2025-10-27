package whispy_server.whispy.domain.notification.model;

import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.global.annotation.Aggregate;

import java.time.LocalDateTime;
import java.util.Map;

@Aggregate
public record Notification(
        Long id,
        String email,
        String title,
        String body,
        NotificationTopic topic,
        Map<String, String> data,
        boolean read,
        LocalDateTime createdAt
) {

    public Notification markAsRead() {
        return new Notification(
                this.id,
                this.email,
                this.title,
                this.body,
                this.topic,
                this.data,
                true,
                this.createdAt
        );
    }
}
