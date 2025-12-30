package whispy_server.whispy.domain.admin.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

/**
 * 관리자 로그아웃 유스케이스.
 * 현재 인증된 관리자의 로그아웃을 처리합니다.
 */
@UseCase
public interface AdminLogoutUseCase {

    /**
     * 현재 인증된 관리자를 로그아웃합니다.
     * Redis에서 리프레시 토큰을 삭제합니다.
     */
    void execute();
}
