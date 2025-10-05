package whispy_server.whispy.domain.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.response.NotificationResponse;
import whispy_server.whispy.domain.notification.application.port.in.QueryMyNotificationsUseCase;
import whispy_server.whispy.domain.notification.application.port.out.QueryNotificationPort;
import whispy_server.whispy.domain.notification.model.Notification;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryMyNotificationsService implements QueryMyNotificationsUseCase {

    private final QueryNotificationPort queryNotificationPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public Page<NotificationResponse> execute(Pageable pageable){
        Page<Notification> notifications = queryNotificationPort.findByEmailOrderByCreatedAtDesc(
                userFacadeUseCase.currentUser().email(),
                pageable
        );

        return notifications.map(NotificationResponse::from);
    }
}
