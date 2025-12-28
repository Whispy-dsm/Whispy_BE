package whispy_server.whispy.domain.user.application.service.unit;
import whispy_server.whispy.domain.user.application.service.UserLogoutService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.global.security.jwt.domain.repository.RefreshTokenRepository;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * UserLogoutService의 단위 테스트 클래스
 *
 * 로그?�웃 ?�비?�의 ?�작??검증합?�다.
 * RefreshToken 삭제를 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserLogoutService 테스트")
class UserLogoutServiceTest {

    @InjectMocks
    private UserLogoutService userLogoutService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;

    @Test
    @DisplayName("로그아웃 시 RefreshToken을 삭제한다")
    void whenLogout_thenDeletesRefreshToken() {
        // given
        User user = createUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);

        // when
        userLogoutService.logout();

        // then
        verify(refreshTokenRepository).deleteById(TEST_USER_ID);
    }

    @Test
    @DisplayName("현재 사용자의 이메일로 RefreshToken을 삭제한다")
    void whenLogout_thenUsesCurrentUserEmail() {
        // given
        User user = createUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);

        // when
        userLogoutService.logout();

        // then
        verify(userFacadeUseCase).currentUser();
        verify(refreshTokenRepository).deleteById(TEST_USER_ID);
    }

    /**
     * 테스트용 User 객체를 생성합니다.
     *
     * @return 생성된 User 객체
     */
    private User createUser() {
        return new User(
                TEST_USER_ID,
                TEST_EMAIL,
                "password",
                "TestUser",
                "https://example.com/profile.jpg",
                Gender.MALE,
                Role.USER,
                "일반 로그인",
                "fcm-token",
                LocalDateTime.now()
        );
    }
}
