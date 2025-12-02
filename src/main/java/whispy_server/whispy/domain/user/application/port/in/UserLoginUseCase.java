package whispy_server.whispy.domain.user.application.port.in;

import whispy_server.whispy.domain.user.adapter.in.web.dto.request.UserLoginRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 사용자 로그인 유스케이스.
 * 이메일과 비밀번호를 사용한 로컬 인증을 처리합니다.
 */
@UseCase
public interface UserLoginUseCase {

    /**
     * 사용자 로그인을 처리하고 JWT 토큰을 발급합니다.
     *
     * @param userLoginRequest 로그인 요청 (이메일, 비밀번호, FCM 토큰)
     * @return JWT 액세스 토큰과 리프레시 토큰
     */
    TokenResponse login(UserLoginRequest  userLoginRequest);
}
