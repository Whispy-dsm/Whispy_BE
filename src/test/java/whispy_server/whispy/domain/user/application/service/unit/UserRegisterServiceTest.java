package whispy_server.whispy.domain.user.application.service.unit;
import whispy_server.whispy.domain.user.application.service.UserRegisterService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.InitializeTopicsRequest;
import whispy_server.whispy.domain.topic.application.port.in.InitializeTopicsUseCase;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.RegisterRequest;
import whispy_server.whispy.domain.user.application.port.out.ExistsUserPort;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.exception.domain.user.UserAlreadyExistException;
import whispy_server.whispy.global.file.DefaultImageProperties;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * UserRegisterService의 단위 테스트 클래스
 *
 * 회원가입 서비스의 다양한 시나리오를 검증합니다.
 * 사용자 등록, 이메일 중복 확인, 비밀번호 암호화, FCM 토픽 초기화를 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserRegisterService 테스트")
class UserRegisterServiceTest {

    @InjectMocks
    private UserRegisterService userRegisterService;

    @Mock
    private UserSavePort userSavePort;

    @Mock
    private ExistsUserPort existsUserPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private InitializeTopicsUseCase initializeTopicsUseCase;

    @Mock
    private DefaultImageProperties defaultImageProperties;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String ENCODED_PASSWORD = "encoded_password123";
    private static final String TEST_FCM_TOKEN = "fcm-token-12345";
    private static final String DEFAULT_IMAGE_URL = "https://example.com/default.jpg";

    @Test
    @DisplayName("유효한 정보로 회원가입을 성공적으로 처리한다")
    void whenValidRegistration_thenRegistersSuccessfully() {
        // given
        RegisterRequest request = createRegisterRequest(true);

        given(existsUserPort.existsByEmail(TEST_EMAIL)).willReturn(false);
        given(passwordEncoder.encode(TEST_PASSWORD)).willReturn(ENCODED_PASSWORD);

        // when
        userRegisterService.register(request);

        // then
        verify(userSavePort).save(any());
        verify(initializeTopicsUseCase).execute(any(InitializeTopicsRequest.class));
    }

    @Test
    @DisplayName("이미 존재하는 이메일인 경우 예외가 발생한다")
    void whenEmailAlreadyExists_thenThrowsException() {
        // given
        RegisterRequest request = createRegisterRequest(true);

        given(existsUserPort.existsByEmail(TEST_EMAIL)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> userRegisterService.register(request))
                .isInstanceOf(UserAlreadyExistException.class);
        verify(userSavePort, never()).save(any());
    }

    @Test
    @DisplayName("비밀번호를 암호화하여 저장한다")
    void whenRegistering_thenEncodesPassword() {
        // given
        RegisterRequest request = createRegisterRequest(true);

        given(existsUserPort.existsByEmail(TEST_EMAIL)).willReturn(false);
        given(passwordEncoder.encode(TEST_PASSWORD)).willReturn(ENCODED_PASSWORD);

        // when
        userRegisterService.register(request);

        // then
        verify(passwordEncoder).encode(TEST_PASSWORD);
    }

    @Test
    @DisplayName("FCM 토큰이 있는 경우 토픽을 초기화한다")
    void whenFcmTokenProvided_thenInitializesTopics() {
        // given
        RegisterRequest request = createRegisterRequest(true);

        given(existsUserPort.existsByEmail(TEST_EMAIL)).willReturn(false);
        given(passwordEncoder.encode(TEST_PASSWORD)).willReturn(ENCODED_PASSWORD);

        // when
        userRegisterService.register(request);

        // then
        verify(initializeTopicsUseCase).execute(any(InitializeTopicsRequest.class));
    }

    @Test
    @DisplayName("FCM 토큰이 없는 경우 토픽 초기화를 건너뛴다")
    void whenNoFcmToken_thenSkipsTopicInitialization() {
        // given
        RegisterRequest request = new RegisterRequest(
                TEST_EMAIL,
                TEST_PASSWORD,
                "TestUser",
                "https://example.com/profile.jpg",
                Gender.MALE,
                null, // FCM 토큰 없음
                true
        );

        given(existsUserPort.existsByEmail(TEST_EMAIL)).willReturn(false);
        given(passwordEncoder.encode(TEST_PASSWORD)).willReturn(ENCODED_PASSWORD);

        // when
        userRegisterService.register(request);

        // then
        verify(initializeTopicsUseCase, never()).execute(any(InitializeTopicsRequest.class));
    }

    @Test
    @DisplayName("프로필 이미지가 없는 경우 기본 이미지를 사용한다")
    void whenNoProfileImage_thenUsesDefaultImage() {
        // given
        RegisterRequest request = new RegisterRequest(
                TEST_EMAIL,
                TEST_PASSWORD,
                "TestUser",
                null, // 프로필 이미지 없음
                Gender.MALE,
                TEST_FCM_TOKEN,
                true
        );

        given(existsUserPort.existsByEmail(TEST_EMAIL)).willReturn(false);
        given(passwordEncoder.encode(TEST_PASSWORD)).willReturn(ENCODED_PASSWORD);
        given(defaultImageProperties.defaultImageUrl()).willReturn(DEFAULT_IMAGE_URL);

        // when
        userRegisterService.register(request);

        // then
        verify(defaultImageProperties).defaultImageUrl();
    }

    @Test
    @DisplayName("이벤트 수신 미동의 시에도 회원가입이 가능하다")
    void whenEventNotAgreed_thenStillRegistersSuccessfully() {
        // given
        RegisterRequest request = createRegisterRequest(false);

        given(existsUserPort.existsByEmail(TEST_EMAIL)).willReturn(false);
        given(passwordEncoder.encode(TEST_PASSWORD)).willReturn(ENCODED_PASSWORD);

        // when
        userRegisterService.register(request);

        // then
        verify(userSavePort).save(any());
        verify(initializeTopicsUseCase).execute(any(InitializeTopicsRequest.class));
    }

    /**
     * 테스트용 RegisterRequest 객체를 생성합니다.
     *
     * @param isEventAgreed 이벤트 수신 동의 여부
     * @return 생성된 RegisterRequest 객체
     */
    private RegisterRequest createRegisterRequest(boolean isEventAgreed) {
        return new RegisterRequest(
                TEST_EMAIL,
                TEST_PASSWORD,
                "TestUser",
                "https://example.com/profile.jpg",
                Gender.MALE,
                TEST_FCM_TOKEN,
                isEventAgreed
        );
    }
}
