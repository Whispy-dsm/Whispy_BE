package whispy_server.whispy.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.domain.user.port.in.UserTokenReissueUseCase;
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
