package whispy_server.whispy.domain.notification.application.port.out;

import whispy_server.whispy.domain.notification.model.Notification;

import java.util.List;

public interface SaveNotificationPort {
    void save(Notification notification);
    void saveAll(List<Notification> notifications);
}
