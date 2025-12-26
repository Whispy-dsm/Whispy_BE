package whispy_server.whispy.domain.user.application.service.unit;
import whispy_server.whispy.domain.user.application.service.UpdateFcmTokenService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.notification.application.port.in.SendToDeviceTokensUseCase;
import whispy_server.whispy.domain.topic.application.port.in.InitializeTopicsUseCase;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.domain.user.facade.UserFacade;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.global.security.jwt.domain.repository.RefreshTokenRepository;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * UpdateFcmTokenService의 단위 테스트 클래스
 *
 * FCM 토큰 업데이트 서비스의 다양한 시나리오를 검증합니다.
 * 토큰 변경, 로그아웃 알림, 세션 무효화를 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateFcmTokenService 테스트")
class UpdateFcmTokenServiceTest {

    @InjectMocks
    private UpdateFcmTokenService updateFcmTokenService;

    @Mock
    private UserFacade userFacade;

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
    private static final String OLD_FCM_TOKEN = "old-fcm-token-12345";
    private static final String NEW_FCM_TOKEN = "new-fcm-token-67890";

    @Test
    @DisplayName("FCM 토큰이 변경된 경우 업데이트를 수행한다")
    void whenTokenChanged_thenUpdatesToken() {
        // given
        User user = createUser(OLD_FCM_TOKEN);
        given(userFacade.currentUser()).willReturn(user);

        // when
        updateFcmTokenService.execute(NEW_FCM_TOKEN);

        // then
        verify(userSavePort).save(any(User.class));
        verify(initializeTopicsUseCase).execute(eq(TEST_EMAIL), eq(NEW_FCM_TOKEN), eq(false));
    }

    @Test
    @DisplayName("토큰 변경 시 이전 기기에 로그아웃 알림을 전송한다")
    void whenTokenChanged_thenSendsLogoutNotification() {
        // given
        User user = createUser(OLD_FCM_TOKEN);
        given(userFacade.currentUser()).willReturn(user);

        // when
        updateFcmTokenService.execute(NEW_FCM_TOKEN);

        // then
        verify(sendToDeviceTokensUseCase).execute(any());
    }

    @Test
    @DisplayName("토큰 변경 시 기존 RefreshToken을 삭제한다")
    void whenTokenChanged_thenDeletesRefreshToken() {
        // given
        User user = createUser(OLD_FCM_TOKEN);
        given(userFacade.currentUser()).willReturn(user);

        // when
        updateFcmTokenService.execute(NEW_FCM_TOKEN);

        // then
        verify(refreshTokenRepository).deleteById(TEST_EMAIL);
    }

    @Test
    @DisplayName("동일한 토큰인 경우 업데이트를 수행하지 않는다")
    void whenSameToken_thenSkipsUpdate() {
        // given
        User user = createUser(OLD_FCM_TOKEN);
        given(userFacade.currentUser()).willReturn(user);

        // when
        updateFcmTokenService.execute(OLD_FCM_TOKEN);

        // then
        verify(userSavePort, never()).save(any());
        verify(sendToDeviceTokensUseCase, never()).execute(any());
        verify(refreshTokenRepository, never()).deleteById(anyString());
    }

    @Test
    @DisplayName("FCM 토큰이 null인 경우 업데이트를 수행하지 않는다")
    void whenNullToken_thenSkipsUpdate() {
        // given
        User user = createUser(OLD_FCM_TOKEN);
        given(userFacade.currentUser()).willReturn(user);

        // when
        updateFcmTokenService.execute(null);

        // then
        verify(userSavePort, never()).save(any());
        verify(sendToDeviceTokensUseCase, never()).execute(any());
    }

    @Test
    @DisplayName("기존 토큰이 없는 사용자도 토큰을 업데이트할 수 있다")
    void whenNoExistingToken_thenUpdatesToken() {
        // given
        User user = createUser(null);
        given(userFacade.currentUser()).willReturn(user);

        // when
        updateFcmTokenService.execute(NEW_FCM_TOKEN);

        // then
        verify(userSavePort).save(any(User.class));
        verify(initializeTopicsUseCase).execute(eq(TEST_EMAIL), eq(NEW_FCM_TOKEN), eq(false));
        verify(sendToDeviceTokensUseCase, never()).execute(any()); // 기존 토큰이 없으므로 알림 미전송
    }

    @Test
    @DisplayName("기존 토큰이 빈 문자열인 경우 로그아웃 알림을 전송하지 않는다")
    void whenEmptyExistingToken_thenSkipsLogoutNotification() {
        // given
        User user = createUser("");
        given(userFacade.currentUser()).willReturn(user);

        // when
        updateFcmTokenService.execute(NEW_FCM_TOKEN);

        // then
        verify(userSavePort).save(any(User.class));
        verify(sendToDeviceTokensUseCase, never()).execute(any());
    }

    @Test
    @DisplayName("로그아웃 알림 전송 실패 시에도 토큰 업데이트는 계속 진행한다")
    void whenLogoutNotificationFails_thenContinuesTokenUpdate() {
        // given
        User user = createUser(OLD_FCM_TOKEN);
        given(userFacade.currentUser()).willReturn(user);
        doThrow(new RuntimeException("Notification failed"))
                .when(sendToDeviceTokensUseCase).execute(any());

        // when
        updateFcmTokenService.execute(NEW_FCM_TOKEN);

        // then
        verify(refreshTokenRepository).deleteById(TEST_EMAIL);
        verify(userSavePort).save(any(User.class));
        verify(initializeTopicsUseCase).execute(eq(TEST_EMAIL), eq(NEW_FCM_TOKEN), eq(false));
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
                "password",
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
