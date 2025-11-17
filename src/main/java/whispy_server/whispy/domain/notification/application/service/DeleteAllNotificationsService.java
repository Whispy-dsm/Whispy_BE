package whispy_server.whispy.domain.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.notification.application.port.in.DeleteAllNotificationsUseCase;
import whispy_server.whispy.domain.notification.application.port.out.DeleteNotificationPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteAllNotificationsService implements DeleteAllNotificationsUseCase {

    private final DeleteNotificationPort deleteNotificationPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public void execute() {
        User currentUser = userFacadeUseCase.currentUser();
        deleteNotificationPort.deleteByEmail(currentUser.email());
    }
}
