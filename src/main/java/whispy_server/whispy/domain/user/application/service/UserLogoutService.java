package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.global.security.jwt.domain.repository.RefreshTokenRepository;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.application.port.in.UserLogoutUseCase;
import whispy_server.whispy.domain.user.model.User;

/**
 * 사용자 로그아웃 서비스.
 * 현재 인증된 사용자의 리프레시 토큰을 삭제하여 로그아웃을 처리합니다.
 */
@Service
@RequiredArgsConstructor
public class UserLogoutService implements UserLogoutUseCase {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 현재 인증된 사용자를 로그아웃합니다.
     * Redis에서 리프레시 토큰을 삭제합니다.
     */
    @Override
    public void logout(){
        User currentUser = userFacadeUseCase.currentUser();
        refreshTokenRepository.deleteById(currentUser.email());
    }
}

