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
import whispy_server.whispy.global.annotation.UserAction;
import whispy_server.whispy.global.exception.domain.fcm.NotificationNotFoundException;

/**
 * 알림 읽음 처리 서비스.
 *
 * 특정 알림을 읽음 상태로 변경하는 유스케이스 구현체입니다.
 * 소유권 검증을 통해 본인의 알림만 읽음 처리할 수 있습니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MarkNotificationAsReadService implements MarkNotificationAsReadUseCase {

    private final QueryNotificationPort queryNotificationPort;
    private final SaveNotificationPort saveNotificationPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 특정 알림을 읽음 상태로 변경합니다.
     *
     * @param notificationId 읽음 처리할 알림 ID
     * @throws NotificationNotFoundException 알림을 찾을 수 없거나 소유자가 아닌 경우
     */
    @UserAction("알림 읽음 처리")
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
