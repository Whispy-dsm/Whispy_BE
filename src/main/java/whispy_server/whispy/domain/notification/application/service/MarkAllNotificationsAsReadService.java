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

/**
 * 모든 알림 읽음 처리 서비스.
 *
 * 현재 사용자의 모든 읽지 않은 알림을 읽음 상태로 변경하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MarkAllNotificationsAsReadService implements MarkAllNotificationsAsReadUseCase {

    private final QueryNotificationPort queryNotificationPort;
    private final SaveNotificationPort saveNotificationPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 모든 알림을 읽음 상태로 변경합니다.
     */
    @Override
    public void execute(){
        User currentUser = userFacadeUseCase.currentUser();
        List<Notification> unreadNotifications = queryNotificationPort.findByEmailAndIsReadFalse(
                currentUser.email()
        );

        List<Notification> readNotifications = unreadNotifications.stream()
                .map(Notification::markAsRead)
                .toList();

        saveNotificationPort.saveAll(readNotifications);
    }
}
