package whispy_server.whispy.domain.fcm.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.fcm.adapter.in.web.dto.response.NotificationResponse;
import whispy_server.whispy.domain.fcm.application.port.in.QueryMyNotificationsUseCase;
import whispy_server.whispy.domain.fcm.application.port.out.QueryNotificationPort;
import whispy_server.whispy.domain.fcm.model.Notification;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryMyNotificationsService implements QueryMyNotificationsUseCase {

    private final QueryNotificationPort queryNotificationPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public List<NotificationResponse> execute(){
        List<Notification> notifications = queryNotificationPort.findByEmailOrderByCreatedAtDesc(
                userFacadeUseCase.currentUser().email()
        );

        return notifications.stream()
                .map(NotificationResponse::from)
                .toList();
    }
}
