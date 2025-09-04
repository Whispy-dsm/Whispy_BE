package whispy_server.whispy.domain.notification.application.port.out;

import java.util.UUID;

public interface DeleteNotificationPort {
    void deleteById(UUID id);
    void deleteAllByEmail(String email);
}
