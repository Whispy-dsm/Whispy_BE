package whispy_server.whispy.domain.notification.application.port.in;

import whispy_server.whispy.domain.notification.adapter.in.web.dto.response.NotificationResponse;

import java.util.List;

public interface QueryMyNotificationsUseCase {
    List<NotificationResponse> execute();
}
