package whispy_server.whispy.domain.notification.application.port.out;

import whispy_server.whispy.domain.notification.model.Notification;

import java.util.List;
import java.util.Optional;

public interface QueryNotificationPort {
    Optional<Notification> findById(Long notificationId);
    List<Notification> findByEmailOrderByCreatedAtDesc(String email);
    List<Notification> findByEmailAndReadFalseOrderByCreatedAtDesc(String email);
    List<Notification> findByEmailAndIsReadFalse(String email);
    int countByEmailAndIsReadFalse(String email);

}
