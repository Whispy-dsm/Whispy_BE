package whispy_server.whispy.domain.notification.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.response.NotificationResponse;

import java.util.List;

public interface QueryMyNotificationsUseCase {
    Page<NotificationResponse> execute(Pageable pageable);
}
