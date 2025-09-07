package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.global.security.jwt.domain.repository.RefreshTokenRepository;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.application.port.in.UserLogoutUseCase;
import whispy_server.whispy.domain.user.model.User;


@Service
@RequiredArgsConstructor
public class UserLogoutService implements UserLogoutUseCase {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public void logout(){
        User currentUser = userFacadeUseCase.currentUser();
        refreshTokenRepository.deleteById(currentUser.email());
    }
}

