package whispy_server.whispy.domain.user.application.port.in;

import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;

/**
 * 카카오 OAuth 로그인 유스케이스.
 * 카카오 액세스 토큰으로 인증하고 JWT 토큰을 발급합니다.
 */
public interface KakaoOauthUseCase {

    /**
     * 카카오 액세스 토큰으로 로그인하고 JWT 토큰을 발급합니다.
     * 신규 사용자의 경우 자동으로 회원가입이 진행됩니다.
     *
     * @param accessToken 카카오에서 발급받은 액세스 토큰
     * @param fcmToken Firebase Cloud Messaging 토큰 (선택)
     * @return JWT 액세스 토큰과 리프레시 토큰
     */
    TokenResponse loginWithKakao(String accessToken, String fcmToken);
}
