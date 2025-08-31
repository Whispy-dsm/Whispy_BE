package whispy_server.whispy.domain.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.notification.application.port.in.MarkAllNotificationsAsReadUseCase;
import whispy_server.whispy.domain.notification.application.port.out.QueryNotificationPort;
import whispy_server.whispy.domain.notification.application.port.out.SaveNotificationPort;
import whispy_server.whispy.domain.notification.model.Notification;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MarkAllNotificationsAsReadService implements MarkAllNotificationsAsReadUseCase {

    private final QueryNotificationPort queryNotificationPort;
    private final SaveNotificationPort saveNotificationPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public void execute(){
        User currentUser = userFacadeUseCase.currentUser();
        List<Notification> unreadNotifications = queryNotificationPort.findByEmailAndIsReadFalse(
                currentUser.email()
        );

        unreadNotifications.forEach(notification -> {
            Notification readNotification = notification.markAsRead();
            saveNotificationPort.save(readNotification);
        });
    }
}
