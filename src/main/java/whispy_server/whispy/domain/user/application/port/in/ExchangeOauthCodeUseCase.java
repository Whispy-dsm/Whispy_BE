package whispy_server.whispy.domain.user.application.port.in;

import whispy_server.whispy.domain.user.adapter.in.web.dto.request.OauthCodeExchangeRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * OAuth 일회용 코드를 JWT 토큰으로 교환하는 유스케이스.
 */
@UseCase
public interface ExchangeOauthCodeUseCase {

    /**
     * 일회용 OAuth 코드를 검증하고 JWT 토큰을 발급한다.
     *
     * @param request 코드 교환 요청
     * @return 발급된 액세스/리프레시 토큰
     */
    TokenResponse execute(OauthCodeExchangeRequest request);
}
