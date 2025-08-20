package whispy_server.whispy.domain.user.application.port.in;

import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;

public interface KakaoOauthUseCase {

    TokenResponse loginWithKakao(String accessToken, String fcmToken);
}
