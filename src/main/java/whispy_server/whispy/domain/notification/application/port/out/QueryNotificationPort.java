package whispy_server.whispy.domain.notification.application.port.out;

import whispy_server.whispy.domain.notification.model.Notification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QueryNotificationPort {
    Optional<Notification> findById(UUID notificationId);
    List<Notification> findByEmailOrderByCreatedAtDesc(String email);
    List<Notification> findByEmailAndIsReadFalseOrderByCreatedAtDesc(String email);
    List<Notification> findByEmailAndIsReadFalse(String email);
    int countByEmailAndIsReadFalse(String email);

}
