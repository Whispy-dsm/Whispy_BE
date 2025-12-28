package whispy_server.whispy.domain.user.application.service.unit;
import whispy_server.whispy.domain.user.application.service.UserTokenReissueService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.TokenReissueRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.global.security.jwt.JwtTokenProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * UserTokenReissueService의 단위 테스트 클래스
 *
 * ?�큰 ?�발�??�비?�의 ?�작??검증합?�다.
 * JwtTokenProvider�??�한 ?�큰 ?�발급을 ?�스?�합?�다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserTokenReissueService 테스트")
class UserTokenReissueServiceTest {

    @InjectMocks
    private UserTokenReissueService userTokenReissueService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private static final String REFRESH_TOKEN = "valid-refresh-token";

    @Test
    @DisplayName("유효한 RefreshToken으로 새로운 토큰을 발급한다")
    void whenValidRefreshToken_thenReissuesToken() {
        // given
        TokenResponse expectedToken = new TokenResponse("new-access-token", "new-refresh-token");
        given(jwtTokenProvider.reissue(REFRESH_TOKEN)).willReturn(expectedToken);

        // when
        TokenResponse response = userTokenReissueService.reissue(new TokenReissueRequest(REFRESH_TOKEN));

        // then
        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("new-access-token");
        assertThat(response.refreshToken()).isEqualTo("new-refresh-token");
    }

    @Test
    @DisplayName("JwtTokenProvider의 reissue 메서드를 호출한다")
    void whenReissuing_thenCallsJwtTokenProvider() {
        // given
        TokenResponse expectedToken = new TokenResponse("new-access-token", "new-refresh-token");
        given(jwtTokenProvider.reissue(REFRESH_TOKEN)).willReturn(expectedToken);

        // when
        userTokenReissueService.reissue(new TokenReissueRequest(REFRESH_TOKEN));

        // then
        verify(jwtTokenProvider).reissue(REFRESH_TOKEN);
    }
}
