package whispy_server.whispy.domain.fcm.model;

import whispy_server.whispy.domain.fcm.model.types.NotificationTopic;
import whispy_server.whispy.global.annotation.Aggregate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Aggregate
public record Notification(
        UUID id,
        String email,
        String title,
        String body,
        NotificationTopic topic,
        Map<String, String> data,
        boolean isRead
) {

    public Notification markAsRead() {
        return new Notification(
                this.id,
                this.email,
                this.title,
                this.body,
                this.topic,
                this.data,
                true
        );
    }
}
