package whispy_server.whispy.domain.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.notification.application.port.in.MarkNotificationAsReadUseCase;
import whispy_server.whispy.domain.notification.application.port.out.QueryNotificationPort;
import whispy_server.whispy.domain.notification.application.port.out.SaveNotificationPort;
import whispy_server.whispy.domain.notification.model.Notification;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.exception.domain.fcm.NotificationNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class MarkNotificationAsReadService implements MarkNotificationAsReadUseCase {

    private final QueryNotificationPort queryNotificationPort;
    private final SaveNotificationPort saveNotificationPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public void execute(Long notificationId){
        User currentUser = userFacadeUseCase.currentUser();
        Notification notification = queryNotificationPort.findById(notificationId)
                .orElseThrow(() -> NotificationNotFoundException.EXCEPTION);

        if(!notification.email().equals(currentUser.email())){
            throw NotificationNotFoundException.EXCEPTION;
        }

        Notification readNotification = notification.markAsRead();
        saveNotificationPort.save(readNotification);
    }
}
