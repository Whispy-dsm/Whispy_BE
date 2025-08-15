package whispy_server.whispy.domain.fcm.application.port.out;

import whispy_server.whispy.domain.fcm.model.Notification;

public interface SaveNotificationPort {
    Notification save(Notification notification);
}
