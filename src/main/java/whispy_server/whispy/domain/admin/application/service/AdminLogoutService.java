package whispy_server.whispy.domain.admin.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.admin.application.port.in.AdminFacadeUseCase;
import whispy_server.whispy.domain.admin.application.port.in.AdminLogoutUseCase;
import whispy_server.whispy.domain.admin.model.Admin;
import whispy_server.whispy.global.annotation.AdminAction;
import whispy_server.whispy.global.security.jwt.domain.repository.RefreshTokenRepository;

/**
 * 관리자 로그아웃 서비스.
 * 현재 인증된 관리자의 리프레시 토큰을 삭제하여 로그아웃을 처리합니다.
 */
@Service
@RequiredArgsConstructor
public class AdminLogoutService implements AdminLogoutUseCase {

    private final RefreshTokenRepository refreshTokenRepository;
    private final AdminFacadeUseCase adminFacadeUseCase;

    /**
     * 현재 인증된 관리자를 로그아웃합니다.
     * Redis에서 리프레시 토큰을 삭제합니다.
     */
    @Override
    @AdminAction("관리자 로그아웃")
    public void execute() {
        Admin currentAdmin = adminFacadeUseCase.currentAdmin();
        refreshTokenRepository.deleteById(currentAdmin.id());
    }
}
