package whispy_server.whispy.domain.fcm.application.port.in;

import whispy_server.whispy.domain.fcm.adapter.in.web.dto.response.NotificationResponse;
import whispy_server.whispy.domain.fcm.model.Notification;

import java.util.List;

public interface QueryMyNotificationsUseCase {
    List<NotificationResponse> execute();
}
