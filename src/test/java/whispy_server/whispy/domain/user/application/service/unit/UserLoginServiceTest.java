package whispy_server.whispy.domain.user.application.service.unit;
import whispy_server.whispy.domain.user.application.service.UserLoginService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import whispy_server.whispy.domain.notification.application.port.in.SendToDeviceTokensUseCase;
import whispy_server.whispy.domain.topic.application.port.in.InitializeTopicsUseCase;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.UserLoginRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.domain.user.application.port.out.QueryUserPort;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.exception.domain.user.PasswordMissMatchException;
import whispy_server.whispy.global.exception.domain.user.UserNotFoundException;
import whispy_server.whispy.global.security.jwt.JwtTokenProvider;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.global.security.jwt.domain.repository.RefreshTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

/**
 * UserLoginService의 단위 테스트 클래스
 *
 * 로그인 서비스의 다양한 시나리오를 검증합니다.
 * 인증, FCM 토큰 업데이트, 세션 무효화 및 JWT 토큰 발급을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserLoginService 테스트")
class UserLoginServiceTest {

    @InjectMocks
    private UserLoginService userLoginService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private QueryUserPort queryUserPort;

    @Mock
    private UserSavePort userSavePort;

    @Mock
    private InitializeTopicsUseCase initializeTopicsUseCase;

    @Mock
    private SendToDeviceTokensUseCase sendToDeviceTokensUseCase;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_PASSWORD = "password123";
    private static final String ENCODED_PASSWORD = "encoded_password";
    private static final String OLD_FCM_TOKEN = "old-fcm-token";
    private static final String NEW_FCM_TOKEN = "new-fcm-token";

    @Test
    @DisplayName("유효한 자격증명으로 로그인에 성공한다")
    void whenValidCredentials_thenLoginSuccessfully() {
        // given
        UserLoginRequest request = new UserLoginRequest(TEST_EMAIL, TEST_PASSWORD, null);
        User user = createUser(null);
        TokenResponse expectedToken = new TokenResponse("access-token", "refresh-token");

        given(queryUserPort.findByEmail(TEST_EMAIL)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(TEST_PASSWORD, ENCODED_PASSWORD)).willReturn(true);
        given(jwtTokenProvider.generateToken(TEST_USER_ID, Role.USER.toString())).willReturn(expectedToken);

        // when
        TokenResponse response = userLoginService.login(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("access-token");
        verify(refreshTokenRepository).deleteById(TEST_USER_ID);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 로그인 시 예외가 발생한다")
    void whenUserNotFound_thenThrowsException() {
        // given
        UserLoginRequest request = new UserLoginRequest(TEST_EMAIL, TEST_PASSWORD, null);

        given(queryUserPort.findByEmail(TEST_EMAIL)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userLoginService.login(request))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 예외가 발생한다")
    void whenPasswordMismatch_thenThrowsException() {
        // given
        UserLoginRequest request = new UserLoginRequest(TEST_EMAIL, "wrongpassword", null);
        User user = createUser(null);

        given(queryUserPort.findByEmail(TEST_EMAIL)).willReturn(Optional.of(user));
        given(passwordEncoder.matches("wrongpassword", ENCODED_PASSWORD)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> userLoginService.login(request))
                .isInstanceOf(PasswordMissMatchException.class);
    }

    @Test
    @DisplayName("로그인 시 기존 세션을 무효화한다")
    void whenLogin_thenInvalidatesExistingSession() {
        // given
        UserLoginRequest request = new UserLoginRequest(TEST_EMAIL, TEST_PASSWORD, null);
        User user = createUser(null);
        TokenResponse expectedToken = new TokenResponse("access-token", "refresh-token");

        given(queryUserPort.findByEmail(TEST_EMAIL)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(TEST_PASSWORD, ENCODED_PASSWORD)).willReturn(true);
        given(jwtTokenProvider.generateToken(TEST_USER_ID, Role.USER.toString())).willReturn(expectedToken);

        // when
        userLoginService.login(request);

        // then
        verify(refreshTokenRepository).deleteById(TEST_USER_ID);
    }

    @Test
    @DisplayName("FCM 토큰이 변경된 경우 토큰을 업데이트한다")
    void whenFcmTokenChanged_thenUpdatesToken() {
        // given
        UserLoginRequest request = new UserLoginRequest(TEST_EMAIL, TEST_PASSWORD, NEW_FCM_TOKEN);
        User user = createUser(OLD_FCM_TOKEN);
        TokenResponse expectedToken = new TokenResponse("access-token", "refresh-token");

        given(queryUserPort.findByEmail(TEST_EMAIL)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(TEST_PASSWORD, ENCODED_PASSWORD)).willReturn(true);
        given(jwtTokenProvider.generateToken(TEST_USER_ID, Role.USER.toString())).willReturn(expectedToken);

        // when
        userLoginService.login(request);

        // then
        verify(userSavePort).save(any(User.class));
        verify(initializeTopicsUseCase).execute(TEST_EMAIL, NEW_FCM_TOKEN, false);
    }

    @Test
    @DisplayName("FCM 토큰 변경 시 이전 기기에 로그아웃 알림을 전송한다")
    void whenFcmTokenChanged_thenSendsLogoutNotification() {
        // given
        UserLoginRequest request = new UserLoginRequest(TEST_EMAIL, TEST_PASSWORD, NEW_FCM_TOKEN);
        User user = createUser(OLD_FCM_TOKEN);
        TokenResponse expectedToken = new TokenResponse("access-token", "refresh-token");

        given(queryUserPort.findByEmail(TEST_EMAIL)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(TEST_PASSWORD, ENCODED_PASSWORD)).willReturn(true);
        given(jwtTokenProvider.generateToken(TEST_USER_ID, Role.USER.toString())).willReturn(expectedToken);

        // when
        userLoginService.login(request);

        // then
        verify(sendToDeviceTokensUseCase).execute(any());
    }

    @Test
    @DisplayName("FCM 토큰이 동일한 경우 토큰 업데이트를 하지 않는다")
    void whenSameFcmToken_thenSkipsTokenUpdate() {
        // given
        UserLoginRequest request = new UserLoginRequest(TEST_EMAIL, TEST_PASSWORD, OLD_FCM_TOKEN);
        User user = createUser(OLD_FCM_TOKEN);
        TokenResponse expectedToken = new TokenResponse("access-token", "refresh-token");

        given(queryUserPort.findByEmail(TEST_EMAIL)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(TEST_PASSWORD, ENCODED_PASSWORD)).willReturn(true);
        given(jwtTokenProvider.generateToken(TEST_USER_ID, Role.USER.toString())).willReturn(expectedToken);

        // when
        userLoginService.login(request);

        // then
        verify(userSavePort, never()).save(any());
        verify(initializeTopicsUseCase, never()).execute(anyString(), anyString(), anyBoolean());
    }

    @Test
    @DisplayName("기존 FCM 토큰이 없는 사용자도 새 토큰으로 로그인할 수 있다")
    void whenNoExistingToken_thenLoginWithNewToken() {
        // given
        UserLoginRequest request = new UserLoginRequest(TEST_EMAIL, TEST_PASSWORD, NEW_FCM_TOKEN);
        User user = createUser(null);
        TokenResponse expectedToken = new TokenResponse("access-token", "refresh-token");

        given(queryUserPort.findByEmail(TEST_EMAIL)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(TEST_PASSWORD, ENCODED_PASSWORD)).willReturn(true);
        given(jwtTokenProvider.generateToken(TEST_USER_ID, Role.USER.toString())).willReturn(expectedToken);

        // when
        userLoginService.login(request);

        // then
        verify(userSavePort).save(any(User.class));
        verify(sendToDeviceTokensUseCase, never()).execute(any()); // 기존 토큰 없으므로 알림 미전송
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
                ENCODED_PASSWORD,
                "TestUser",
                "https://example.com/profile.jpg",
                Gender.MALE,
                Role.USER,
                "일반 로그인",
                fcmToken,
                LocalDateTime.now()
        );
    }
}
