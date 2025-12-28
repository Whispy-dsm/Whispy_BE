package whispy_server.whispy.domain.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.notification.application.port.in.DeleteNotificationUseCase;
import whispy_server.whispy.domain.notification.application.port.out.DeleteNotificationPort;
import whispy_server.whispy.domain.notification.application.port.out.QueryNotificationPort;
import whispy_server.whispy.domain.notification.model.Notification;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.annotation.UserAction;
import whispy_server.whispy.global.exception.domain.fcm.NotificationNotFoundException;

/**
 * 알림 삭제 서비스.
 *
 * 특정 알림을 삭제하는 유스케이스 구현체입니다.
 * 소유권 검증을 통해 본인의 알림만 삭제할 수 있습니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DeleteNotificationService implements DeleteNotificationUseCase {

    private final QueryNotificationPort queryNotificationPort;
    private final DeleteNotificationPort deleteNotificationPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 특정 알림을 삭제합니다.
     *
     * @param notificationId 삭제할 알림 ID
     * @throws NotificationNotFoundException 알림을 찾을 수 없거나 소유자가 아닌 경우
     */
    @UserAction("알림 삭제")
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
