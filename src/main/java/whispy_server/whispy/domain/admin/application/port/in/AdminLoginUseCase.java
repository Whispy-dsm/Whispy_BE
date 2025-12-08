package whispy_server.whispy.domain.admin.application.port.in;

import whispy_server.whispy.domain.admin.adapter.in.web.dto.request.AdminLoginRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;

/**
 * 관리자 로그인 유스케이스.
 *
 * 관리자 인증을 처리하고 JWT 토큰을 발급하는 인바운드 포트입니다.
 */
public interface AdminLoginUseCase {
    /**
     * 관리자 로그인을 실행합니다.
     *
     * @param request 관리자 ID와 비밀번호가 포함된 요청
     * @return JWT 액세스 토큰과 리프레시 토큰
     */
    TokenResponse execute(AdminLoginRequest request);
}
