package whispy_server.whispy.domain.user.application.service.unit;
import whispy_server.whispy.domain.user.application.service.ResetPasswordService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.ResetPasswordRequest;
import whispy_server.whispy.domain.user.application.port.out.QueryUserPort;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.exception.domain.auth.EmailNotVerifiedException;
import whispy_server.whispy.global.exception.domain.user.UserNotFoundException;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.global.utils.redis.RedisUtil;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * ResetPasswordService의 단위 테스트 클래스
 *
 * 비밀번호 재설정 서비스의 다양한 시나리오를 검증합니다.
 * 이메일 인증 확인, 비밀번호 암호화 및 Redis 상태 삭제를 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ResetPasswordService 테스트")
class ResetPasswordServiceTest {

    @InjectMocks
    private ResetPasswordService resetPasswordService;

    @Mock
    private QueryUserPort queryUserPort;

    @Mock
    private UserSavePort userSavePort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RedisUtil redisUtil;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;
    private static final String NEW_PASSWORD = "newPassword123";
    private static final String ENCODED_PASSWORD = "encoded_newPassword123";

    @Test
    @DisplayName("이메일 인증이 완료된 경우 비밀번호를 재설정한다")
    void whenEmailVerified_thenResetsPassword() {
        // given
        ResetPasswordRequest request = new ResetPasswordRequest(TEST_EMAIL, NEW_PASSWORD);
        User user = createUser();
        String statusKey = "email:verification:status:" + TEST_EMAIL;

        given(redisUtil.get(statusKey)).willReturn("verified");
        given(queryUserPort.findByEmail(TEST_EMAIL)).willReturn(Optional.of(user));
        given(passwordEncoder.encode(NEW_PASSWORD)).willReturn(ENCODED_PASSWORD);

        // when
        resetPasswordService.execute(request);

        // then
        verify(userSavePort).save(any(User.class));
        verify(redisUtil).delete(statusKey);
    }

    @Test
    @DisplayName("이메일이 인증되지 않은 경우 예외가 발생한다")
    void whenEmailNotVerified_thenThrowsException() {
        // given
        ResetPasswordRequest request = new ResetPasswordRequest(TEST_EMAIL, NEW_PASSWORD);
        String statusKey = "email:verification:status:" + TEST_EMAIL;

        given(redisUtil.get(statusKey)).willReturn(null);

        // when & then
        assertThatThrownBy(() -> resetPasswordService.execute(request))
                .isInstanceOf(EmailNotVerifiedException.class);
    }

    @Test
    @DisplayName("존재하지 않는 사용자인 경우 예외가 발생한다")
    void whenUserNotFound_thenThrowsException() {
        // given
        ResetPasswordRequest request = new ResetPasswordRequest(TEST_EMAIL, NEW_PASSWORD);
        String statusKey = "email:verification:status:" + TEST_EMAIL;

        given(redisUtil.get(statusKey)).willReturn("verified");
        given(queryUserPort.findByEmail(TEST_EMAIL)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> resetPasswordService.execute(request))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("비밀번호 재설정 시 새 비밀번호를 암호화한다")
    void whenResettingPassword_thenEncodesNewPassword() {
        // given
        ResetPasswordRequest request = new ResetPasswordRequest(TEST_EMAIL, NEW_PASSWORD);
        User user = createUser();
        String statusKey = "email:verification:status:" + TEST_EMAIL;

        given(redisUtil.get(statusKey)).willReturn("verified");
        given(queryUserPort.findByEmail(TEST_EMAIL)).willReturn(Optional.of(user));
        given(passwordEncoder.encode(NEW_PASSWORD)).willReturn(ENCODED_PASSWORD);

        // when
        resetPasswordService.execute(request);

        // then
        verify(passwordEncoder).encode(NEW_PASSWORD);
    }

    @Test
    @DisplayName("비밀번호 재설정 후 Redis 인증 상태를 삭제한다")
    void whenPasswordReset_thenDeletesVerificationStatus() {
        // given
        ResetPasswordRequest request = new ResetPasswordRequest(TEST_EMAIL, NEW_PASSWORD);
        User user = createUser();
        String statusKey = "email:verification:status:" + TEST_EMAIL;

        given(redisUtil.get(statusKey)).willReturn("verified");
        given(queryUserPort.findByEmail(TEST_EMAIL)).willReturn(Optional.of(user));
        given(passwordEncoder.encode(NEW_PASSWORD)).willReturn(ENCODED_PASSWORD);

        // when
        resetPasswordService.execute(request);

        // then
        verify(redisUtil).delete(statusKey);
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
                "oldPassword",
                "TestUser",
                null,
                Gender.MALE,
                Role.USER,
                "일반 로그인",
                null,
                LocalDateTime.now()
        );
    }
}
