package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.ChangePasswordRequest;
import whispy_server.whispy.domain.user.application.port.in.ChangePasswordUseCase;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.exception.domain.user.PasswordMissMatchException;

@Service
@RequiredArgsConstructor
public class ChangePasswordService implements ChangePasswordUseCase {

    private final UserFacadeUseCase userFacadeUseCase;
    private final UserSavePort userSavePort;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void execute(ChangePasswordRequest request) {

        User currentUser = userFacadeUseCase.currentUser();

        if (!passwordEncoder.matches(request.currentPassword(), currentUser.password())) {
            throw PasswordMissMatchException.EXCEPTION;
        }

        String encodedNewPassword = passwordEncoder.encode(request.newPassword());
        User updatedUser = currentUser.updatePassword(encodedNewPassword);

        userSavePort.save(updatedUser);
    }
}
