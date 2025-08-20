package whispy_server.whispy.domain.fcm.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.fcm.application.port.in.InitializeTopicsUseCase;
import whispy_server.whispy.domain.fcm.application.port.in.UpdateFcmTokenUseCase;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.domain.user.facade.UserFacade;
import whispy_server.whispy.domain.user.model.User;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateFcmTokenService implements UpdateFcmTokenUseCase {

    private final UserFacade userFacade;
    private final UserSavePort userSavePort;
    private final InitializeTopicsUseCase initializeTopicsUseCase;

    @Override
    public void execute(String fcmToken) {
        User currentUser = userFacade.currentUser();

        if (fcmToken != null && !fcmToken.equals(currentUser.fcmToken())) {
            User updatedUser = currentUser.updateFcmToken(fcmToken);
            userSavePort.save(updatedUser);

            initializeTopicsUseCase.execute(currentUser.email(), fcmToken);
        }
    }
}
