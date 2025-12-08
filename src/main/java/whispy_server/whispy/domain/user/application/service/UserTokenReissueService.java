package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.domain.user.application.port.in.UserTokenReissueUseCase;
import whispy_server.whispy.global.security.jwt.JwtTokenProvider;

/**
 * 토큰 재발급 서비스.
 * 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급합니다.
 */
@Service
@RequiredArgsConstructor
public class UserTokenReissueService implements UserTokenReissueUseCase {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 리프레시 토큰으로 새로운 액세스 토큰을 재발급합니다.
     *
     * @param token 리프레시 토큰
     * @return 새로운 JWT 액세스 토큰과 리프레시 토큰
     */
    @Transactional
    @Override
    public TokenResponse reissue(String token) {
        return jwtTokenProvider.reissue(token);
    }
}
