package whispy_server.whispy.domain.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.notification.application.port.in.DeleteAllNotificationsUseCase;
import whispy_server.whispy.domain.notification.application.port.out.DeleteNotificationPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.annotation.UserAction;

/**
 * 모든 알림 삭제 서비스.
 *
 * 현재 사용자의 모든 알림을 삭제하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DeleteAllNotificationsService implements DeleteAllNotificationsUseCase {

    private final DeleteNotificationPort deleteNotificationPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 모든 알림을 삭제합니다.
     */
    @UserAction("모든 알림 삭제")
    @Override
    public void execute() {
        User currentUser = userFacadeUseCase.currentUser();
        deleteNotificationPort.deleteByEmail(currentUser.email());
    }
}
