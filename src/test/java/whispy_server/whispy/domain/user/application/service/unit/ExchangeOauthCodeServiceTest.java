package whispy_server.whispy.domain.user.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.OauthCodeExchangeRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.domain.user.application.service.ExchangeOauthCodeService;
import whispy_server.whispy.global.exception.domain.oauth.InvalidOrExpiredOauthCodeException;
import whispy_server.whispy.global.oauth.OauthCodeConstants;
import whispy_server.whispy.global.security.jwt.JwtTokenProvider;
import whispy_server.whispy.global.utils.redis.RedisUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExchangeOauthCodeService 테스트")
class ExchangeOauthCodeServiceTest {

    @InjectMocks
    private ExchangeOauthCodeService exchangeOauthCodeService;

    @Mock
    private RedisUtil redisUtil;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("유효한 OAuth 코드로 토큰 교환에 성공한다")
    void whenValidCode_thenReturnTokenResponse() {
        // given
        String code = "test-code";
        String key = OauthCodeConstants.oauthCodeKey(code);
        TokenResponse expected = new TokenResponse("access-token", "refresh-token");

        given(redisUtil.getAndDelete(key)).willReturn("1:USER");
        given(jwtTokenProvider.generateToken(1L, "USER")).willReturn(expected);

        // when
        TokenResponse result = exchangeOauthCodeService.execute(new OauthCodeExchangeRequest(code));

        // then
        assertThat(result).isEqualTo(expected);
        verify(redisUtil).getAndDelete(key);
        verify(jwtTokenProvider).generateToken(1L, "USER");
    }

    @Test
    @DisplayName("만료되었거나 존재하지 않는 OAuth 코드는 예외가 발생한다")
    void whenCodeMissing_thenThrowException() {
        // given
        String code = "expired-code";
        String key = OauthCodeConstants.oauthCodeKey(code);
        given(redisUtil.getAndDelete(key)).willReturn(null);

        // when & then
        assertThatThrownBy(() -> exchangeOauthCodeService.execute(new OauthCodeExchangeRequest(code)))
                .isInstanceOf(InvalidOrExpiredOauthCodeException.class);

        verify(jwtTokenProvider, never()).generateToken(anyLong(), anyString());
    }

    @Test
    @DisplayName("OAuth 코드 payload 형식이 잘못되면 예외가 발생한다")
    void whenInvalidPayload_thenThrowException() {
        // given
        String code = "invalid-payload-code";
        String key = OauthCodeConstants.oauthCodeKey(code);
        given(redisUtil.getAndDelete(key)).willReturn("invalid");

        // when & then
        assertThatThrownBy(() -> exchangeOauthCodeService.execute(new OauthCodeExchangeRequest(code)))
                .isInstanceOf(InvalidOrExpiredOauthCodeException.class);

        verify(jwtTokenProvider, never()).generateToken(anyLong(), anyString());
    }

    @Test
    @DisplayName("OAuth 코드 payload의 사용자 ID가 숫자가 아니면 예외가 발생한다")
    void whenInvalidUserIdInPayload_thenThrowException() {
        // given
        String code = "invalid-user-id-code";
        String key = OauthCodeConstants.oauthCodeKey(code);
        given(redisUtil.getAndDelete(key)).willReturn("not-a-number:USER");

        // when & then
        assertThatThrownBy(() -> exchangeOauthCodeService.execute(new OauthCodeExchangeRequest(code)))
                .isInstanceOf(InvalidOrExpiredOauthCodeException.class);

        verify(jwtTokenProvider, never()).generateToken(anyLong(), anyString());
    }

    @Test
    @DisplayName("OAuth 코드 payload의 권한 값이 비어 있으면 예외가 발생한다")
    void whenBlankRoleInPayload_thenThrowException() {
        // given
        String code = "blank-role-code";
        String key = OauthCodeConstants.oauthCodeKey(code);
        given(redisUtil.getAndDelete(key)).willReturn("1:   ");

        // when & then
        assertThatThrownBy(() -> exchangeOauthCodeService.execute(new OauthCodeExchangeRequest(code)))
                .isInstanceOf(InvalidOrExpiredOauthCodeException.class);

        verify(jwtTokenProvider, never()).generateToken(anyLong(), anyString());
    }
}
