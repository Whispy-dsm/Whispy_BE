package whispy_server.whispy.domain.user.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.notification.application.port.in.SendToDeviceTokensUseCase;
import whispy_server.whispy.domain.topic.application.port.in.InitializeTopicsUseCase;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.domain.user.adapter.out.external.KakaoUserInfoAdapter;
import whispy_server.whispy.domain.user.application.port.in.OauthUserUseCase;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.domain.user.application.service.KakaoOauthService;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.oauth.dto.OauthUserInfo;
import whispy_server.whispy.global.security.jwt.JwtTokenProvider;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.global.security.jwt.domain.repository.RefreshTokenRepository;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * KakaoOauthService의 단위 테스트 클래스
 *
 * 카카오 OAuth 로그인 서비스의 다양한 시나리오를 검증합니다.
 * 카카오 사용자 정보 조회, 신규/기존 사용자 처리, FCM 토큰 업데이트 및 로그아웃 알림을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("KakaoOauthService 테스트")
class KakaoOauthServiceTest {

    @InjectMocks
    private KakaoOauthService kakaoOauthService;

    @Mock
    private KakaoUserInfoAdapter kakaoUserInfoAdapter;

    @Mock
    private OauthUserUseCase oauthUserUseCase;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private InitializeTopicsUseCase initializeTopicsUseCase;

    @Mock
    private UserSavePort userSavePort;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private SendToDeviceTokensUseCase sendToDeviceTokensUseCase;

    private static final String TEST_EMAIL = "kakao@example.com";
    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_ACCESS_TOKEN = "kakao-access-token";
    private static final String OLD_FCM_TOKEN = "old-fcm-token";
    private static final String NEW_FCM_TOKEN = "new-fcm-token";

    @Test
    @DisplayName("카카오 액세스 토큰으로 로그인에 성공한다")
    void whenValidKakaoToken_thenLoginSuccessfully() {
        // given
        Map<String, Object> userAttribute = createKakaoUserAttribute();
        User user = createUser(null);
        TokenResponse expectedToken = new TokenResponse("access-token", "refresh-token");

        given(kakaoUserInfoAdapter.fetchUserInfo(TEST_ACCESS_TOKEN)).willReturn(userAttribute);
        given(oauthUserUseCase.findOrCreateOauthUser(any(OauthUserInfo.class), eq("kakao"))).willReturn(user);
        given(jwtTokenProvider.generateToken(TEST_EMAIL, Role.USER.name())).willReturn(expectedToken);

        // when
        TokenResponse response = kakaoOauthService.loginWithKakao(TEST_ACCESS_TOKEN, null);

        // then
        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("access-token");
        verify(refreshTokenRepository).deleteById(TEST_EMAIL);
    }

    @Test
    @DisplayName("FCM 토큰이 제공되고 변경된 경우 토큰을 업데이트한다")
    void whenFcmTokenChanged_thenUpdatesToken() {
        // given
        Map<String, Object> userAttribute = createKakaoUserAttribute();
        User user = createUser(OLD_FCM_TOKEN);
        TokenResponse expectedToken = new TokenResponse("access-token", "refresh-token");

        given(kakaoUserInfoAdapter.fetchUserInfo(TEST_ACCESS_TOKEN)).willReturn(userAttribute);
        given(oauthUserUseCase.findOrCreateOauthUser(any(OauthUserInfo.class), eq("kakao"))).willReturn(user);
        given(jwtTokenProvider.generateToken(TEST_EMAIL, Role.USER.name())).willReturn(expectedToken);

        // when
        kakaoOauthService.loginWithKakao(TEST_ACCESS_TOKEN, NEW_FCM_TOKEN);

        // then
        verify(userSavePort).save(any(User.class));
        verify(initializeTopicsUseCase).execute(TEST_EMAIL, NEW_FCM_TOKEN, false);
    }

    @Test
    @DisplayName("FCM 토큰 변경 시 이전 기기에 로그아웃 알림을 전송한다")
    void whenFcmTokenChanged_thenSendsLogoutNotification() {
        // given
        Map<String, Object> userAttribute = createKakaoUserAttribute();
        User user = createUser(OLD_FCM_TOKEN);
        TokenResponse expectedToken = new TokenResponse("access-token", "refresh-token");

        given(kakaoUserInfoAdapter.fetchUserInfo(TEST_ACCESS_TOKEN)).willReturn(userAttribute);
        given(oauthUserUseCase.findOrCreateOauthUser(any(OauthUserInfo.class), eq("kakao"))).willReturn(user);
        given(jwtTokenProvider.generateToken(TEST_EMAIL, Role.USER.name())).willReturn(expectedToken);

        // when
        kakaoOauthService.loginWithKakao(TEST_ACCESS_TOKEN, NEW_FCM_TOKEN);

        // then
        verify(sendToDeviceTokensUseCase).execute(any());
    }

    @Test
    @DisplayName("FCM 토큰이 동일한 경우 토큰 업데이트를 하지 않는다")
    void whenSameFcmToken_thenSkipsTokenUpdate() {
        // given
        Map<String, Object> userAttribute = createKakaoUserAttribute();
        User user = createUser(OLD_FCM_TOKEN);
        TokenResponse expectedToken = new TokenResponse("access-token", "refresh-token");

        given(kakaoUserInfoAdapter.fetchUserInfo(TEST_ACCESS_TOKEN)).willReturn(userAttribute);
        given(oauthUserUseCase.findOrCreateOauthUser(any(OauthUserInfo.class), eq("kakao"))).willReturn(user);
        given(jwtTokenProvider.generateToken(TEST_EMAIL, Role.USER.name())).willReturn(expectedToken);

        // when
        kakaoOauthService.loginWithKakao(TEST_ACCESS_TOKEN, OLD_FCM_TOKEN);

        // then
        verify(userSavePort, never()).save(any());
        verify(initializeTopicsUseCase, never()).execute(anyString(), anyString(), anyBoolean());
    }

    @Test
    @DisplayName("기존 FCM 토큰이 없는 사용자도 새 토큰으로 로그인할 수 있다")
    void whenNoExistingToken_thenLoginWithNewToken() {
        // given
        Map<String, Object> userAttribute = createKakaoUserAttribute();
        User user = createUser(null);
        TokenResponse expectedToken = new TokenResponse("access-token", "refresh-token");

        given(kakaoUserInfoAdapter.fetchUserInfo(TEST_ACCESS_TOKEN)).willReturn(userAttribute);
        given(oauthUserUseCase.findOrCreateOauthUser(any(OauthUserInfo.class), eq("kakao"))).willReturn(user);
        given(jwtTokenProvider.generateToken(TEST_EMAIL, Role.USER.name())).willReturn(expectedToken);

        // when
        kakaoOauthService.loginWithKakao(TEST_ACCESS_TOKEN, NEW_FCM_TOKEN);

        // then
        verify(userSavePort).save(any(User.class));
        verify(sendToDeviceTokensUseCase, never()).execute(any()); // 기존 토큰 없으므로 알림 미전송
    }

    @Test
    @DisplayName("기존 FCM 토큰이 빈 문자열인 경우 로그아웃 알림을 보내지 않는다")
    void whenExistingTokenIsEmpty_thenDoesNotSendLogoutNotification() {
        // given
        Map<String, Object> userAttribute = createKakaoUserAttribute();
        User user = createUser("   ");
        TokenResponse expectedToken = new TokenResponse("access-token", "refresh-token");

        given(kakaoUserInfoAdapter.fetchUserInfo(TEST_ACCESS_TOKEN)).willReturn(userAttribute);
        given(oauthUserUseCase.findOrCreateOauthUser(any(OauthUserInfo.class), eq("kakao"))).willReturn(user);
        given(jwtTokenProvider.generateToken(TEST_EMAIL, Role.USER.name())).willReturn(expectedToken);

        // when
        kakaoOauthService.loginWithKakao(TEST_ACCESS_TOKEN, NEW_FCM_TOKEN);

        // then
        verify(userSavePort).save(any(User.class));
        verify(sendToDeviceTokensUseCase, never()).execute(any());
    }

    @Test
    @DisplayName("로그인 시 기존 세션을 무효화한다")
    void whenLogin_thenInvalidatesExistingSession() {
        // given
        Map<String, Object> userAttribute = createKakaoUserAttribute();
        User user = createUser(null);
        TokenResponse expectedToken = new TokenResponse("access-token", "refresh-token");

        given(kakaoUserInfoAdapter.fetchUserInfo(TEST_ACCESS_TOKEN)).willReturn(userAttribute);
        given(oauthUserUseCase.findOrCreateOauthUser(any(OauthUserInfo.class), eq("kakao"))).willReturn(user);
        given(jwtTokenProvider.generateToken(TEST_EMAIL, Role.USER.name())).willReturn(expectedToken);

        // when
        kakaoOauthService.loginWithKakao(TEST_ACCESS_TOKEN, null);

        // then
        verify(refreshTokenRepository).deleteById(TEST_EMAIL);
    }

    @Test
    @DisplayName("알림 전송 실패 시에도 로그인은 계속 진행된다")
    void whenNotificationFails_thenLoginContinues() {
        // given
        Map<String, Object> userAttribute = createKakaoUserAttribute();
        User user = createUser(OLD_FCM_TOKEN);
        TokenResponse expectedToken = new TokenResponse("access-token", "refresh-token");

        given(kakaoUserInfoAdapter.fetchUserInfo(TEST_ACCESS_TOKEN)).willReturn(userAttribute);
        given(oauthUserUseCase.findOrCreateOauthUser(any(OauthUserInfo.class), eq("kakao"))).willReturn(user);
        given(jwtTokenProvider.generateToken(TEST_EMAIL, Role.USER.name())).willReturn(expectedToken);
        doThrow(new RuntimeException("FCM 전송 실패")).when(sendToDeviceTokensUseCase).execute(any());

        // when
        TokenResponse response = kakaoOauthService.loginWithKakao(TEST_ACCESS_TOKEN, NEW_FCM_TOKEN);

        // then
        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("access-token");
    }

    /**
     * 테스트용 User 객체를 생성합니다.
     *
     * @param fcmToken FCM 토큰
     * @return 생성된 User 객체
     */
    private User createUser(String fcmToken) {
        return new User(
                TEST_USER_ID,
                TEST_EMAIL,
                null,
                "KakaoUser",
                "https://example.com/profile.jpg",
                Gender.UNKNOWN,
                Role.USER,
                "KAKAO",
                fcmToken,
                LocalDateTime.now()
        );
    }

    /**
     * 테스트용 카카오 사용자 속성 Map을 생성합니다.
     *
     * @return 카카오 사용자 정보가 포함된 Map
     */
    private Map<String, Object> createKakaoUserAttribute() {
        return Map.of(
                "id", 123456789L,
                "kakao_account", Map.of(
                        "email", TEST_EMAIL,
                        "profile", Map.of(
                                "nickname", "KakaoUser",
                                "profile_image_url", "https://example.com/profile.jpg"
                        )
                )
        );
    }
}
