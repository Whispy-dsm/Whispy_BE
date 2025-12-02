package whispy_server.whispy.domain.user.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

/**
 * 사용자 로그아웃 유스케이스.
 * 현재 인증된 사용자의 로그아웃을 처리합니다.
 */
@UseCase
public interface UserLogoutUseCase {

    /**
     * 현재 인증된 사용자를 로그아웃합니다.
     * Redis에서 리프레시 토큰을 삭제합니다.
     */
    void logout();
}
