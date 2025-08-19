package whispy_server.whispy.domain.fcm.application.port.out;

import whispy_server.whispy.domain.fcm.model.Notification;

import java.util.List;

public interface SaveNotificationPort {
    Notification save(Notification notification);
    List<Notification> saveAll(List<Notification> notifications);
}
