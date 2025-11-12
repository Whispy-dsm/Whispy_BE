package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.ChangeProfileRequest;
import whispy_server.whispy.domain.user.application.port.in.ChangeProfileUseCase;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.domain.user.model.User;

@Service
@RequiredArgsConstructor
public class ChangeProfileService implements ChangeProfileUseCase {

    private final UserFacadeUseCase userFacadeUseCase;
    private final UserSavePort userSavePort;

    @Override
    @Transactional
    public void execute(ChangeProfileRequest request) {
        User user = userFacadeUseCase.currentUser();

        User updateUser = user.changeProfile(request.name(), request.profileImageUrl(), request.gender());
        userSavePort.save(updateUser);

    }
}
