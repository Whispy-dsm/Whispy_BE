package whispy_server.whispy.domain.user.application.port.in;

import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 토큰 재발급 유스케이스.
 * 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급합니다.
 */
@UseCase
public interface UserTokenReissueUseCase {

    /**
     * 리프레시 토큰으로 새로운 액세스 토큰을 재발급합니다.
     *
     * @param token 리프레시 토큰
     * @return 새로운 JWT 액세스 토큰과 리프레시 토큰
     */
    TokenResponse reissue(String token);
}
