package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.OauthCodeExchangeRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.domain.user.application.port.in.ExchangeOauthCodeUseCase;
import whispy_server.whispy.global.exception.domain.oauth.InvalidOrExpiredOauthCodeException;
import whispy_server.whispy.global.oauth.OauthCodeConstants;
import whispy_server.whispy.global.security.jwt.JwtTokenProvider;
import whispy_server.whispy.global.utils.redis.RedisUtil;

/**
 * OAuth 딥링크 일회용 코드를 실제 JWT 토큰으로 교환하는 서비스.
 */
@Service
@RequiredArgsConstructor
public class ExchangeOauthCodeService implements ExchangeOauthCodeUseCase {

    private final RedisUtil redisUtil;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Redis에 저장된 one-time code를 1회성으로 소비해 JWT를 발급한다.
     *
     * @param request 코드 교환 요청
     * @return 발급된 액세스/리프레시 토큰
     */
    @Override
    @Transactional
    public TokenResponse execute(OauthCodeExchangeRequest request) {
        String key = OauthCodeConstants.oauthCodeKey(request.code());
        String codePayload = redisUtil.getAndDelete(key);

        if (codePayload == null) {
            throw InvalidOrExpiredOauthCodeException.EXCEPTION;
        }

        String[] payloadParts = codePayload.split(":", 2);
        if (payloadParts.length != 2 || payloadParts[1].isBlank()) {
            throw InvalidOrExpiredOauthCodeException.EXCEPTION;
        }

        try {
            Long userId = Long.parseLong(payloadParts[0]);
            String role = payloadParts[1];
            return jwtTokenProvider.generateToken(userId, role);
        } catch (NumberFormatException e) {
            throw InvalidOrExpiredOauthCodeException.EXCEPTION;
        }
    }
}
