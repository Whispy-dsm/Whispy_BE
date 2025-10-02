package whispy_server.whispy.domain.notification.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.notification.model.Notification;

import java.util.List;
import java.util.Optional;

public interface QueryNotificationPort {
    Optional<Notification> findById(Long notificationId);
    Page<Notification> findByEmailOrderByCreatedAtDesc(String email, Pageable pageable);
    List<Notification> findByEmailAndReadFalseOrderByCreatedAtDesc(String email);
    List<Notification> findByEmailAndIsReadFalse(String email);
    int countByEmailAndIsReadFalse(String email);

}
