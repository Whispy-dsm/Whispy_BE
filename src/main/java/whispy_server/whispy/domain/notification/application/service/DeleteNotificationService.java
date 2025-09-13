package whispy_server.whispy.domain.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.notification.application.port.in.DeleteNotificationUseCase;
import whispy_server.whispy.domain.notification.application.port.out.DeleteNotificationPort;
import whispy_server.whispy.domain.notification.application.port.out.NotificationPort;
import whispy_server.whispy.domain.notification.application.port.out.QueryNotificationPort;
import whispy_server.whispy.domain.notification.model.Notification;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.exception.domain.fcm.NotificationNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteNotificationService implements DeleteNotificationUseCase {

    private final QueryNotificationPort queryNotificationPort;
    private final DeleteNotificationPort deleteNotificationPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public void execute(Long notificationId) {
        User currentUser = userFacadeUseCase.currentUser();
        Notification notification = queryNotificationPort.findById(notificationId)
                .orElseThrow(() -> NotificationNotFoundException.EXCEPTION);

        if (!notification.email().equals(currentUser.email())) {
            throw NotificationNotFoundException.EXCEPTION;
        }

        deleteNotificationPort.deleteById(notificationId);
    }
}
