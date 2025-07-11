package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.domain.user.application.port.in.UserTokenReissueUseCase;
import whispy_server.whispy.global.security.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class UserTokenReissueService implements UserTokenReissueUseCase {

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public TokenResponse reissue(String token) {
        return jwtTokenProvider.reissue(token);
    }
}
