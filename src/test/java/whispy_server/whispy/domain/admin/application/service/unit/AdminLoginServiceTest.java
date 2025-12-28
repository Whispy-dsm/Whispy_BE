package whispy_server.whispy.domain.admin.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import whispy_server.whispy.domain.admin.adapter.in.web.dto.request.AdminLoginRequest;
import whispy_server.whispy.domain.admin.application.port.out.QueryAdminPort;
import whispy_server.whispy.domain.admin.application.service.AdminLoginService;
import whispy_server.whispy.domain.admin.model.Admin;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.global.exception.domain.admin.AdminNotFoundException;
import whispy_server.whispy.global.exception.domain.user.PasswordMissMatchException;
import whispy_server.whispy.global.security.jwt.JwtProperties;
import whispy_server.whispy.global.security.jwt.JwtTokenProvider;
import whispy_server.whispy.global.security.jwt.domain.entity.RefreshToken;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.global.security.jwt.domain.repository.RefreshTokenRepository;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * AdminLoginService의 단위 테스트 클래스
 *
 * 관리자 로그인 서비스의 다양한 시나리오를 검증합니다.
 * 인증, 비밀번호 검증, JWT 토큰 발급 및 리프레시 토큰 저장을 테스트합니다.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AdminLoginService 테스트")
class AdminLoginServiceTest {

    @InjectMocks
    private AdminLoginService adminLoginService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private QueryAdminPort queryAdminPort;

    @Mock
    private JwtProperties jwtProperties;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private static final Long TEST_ADMIN_ID = 1L;
    private static final String TEST_ADMIN_LOGIN_ID = "admin";
    private static final String TEST_PASSWORD = "adminpass123";
    private static final String ENCODED_PASSWORD = "encoded_password";
    private static final Long REFRESH_EXPIRATION = 604800000L;

    @Test
    @DisplayName("유효한 자격증명으로 관리자 로그인에 성공한다")
    void whenValidCredentials_thenLoginSuccessfully() {
        // given
        AdminLoginRequest request = new AdminLoginRequest(TEST_ADMIN_LOGIN_ID, TEST_PASSWORD);
        Admin admin = createAdmin();
        TokenResponse expectedToken = new TokenResponse("access-token", "refresh-token");

        given(queryAdminPort.findByAdminId(TEST_ADMIN_LOGIN_ID)).willReturn(Optional.of(admin));
        given(passwordEncoder.matches(TEST_PASSWORD, ENCODED_PASSWORD)).willReturn(true);
        given(jwtTokenProvider.generateToken(TEST_ADMIN_ID, Role.ADMIN.name())).willReturn(expectedToken);
        given(jwtProperties.refreshExpiration()).willReturn(REFRESH_EXPIRATION);

        // when
        TokenResponse response = adminLoginService.execute(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
    }

    @Test
    @DisplayName("로그인 성공 시 리프레시 토큰을 Redis에 저장한다")
    void whenLoginSuccess_thenSavesRefreshToken() {
        // given
        AdminLoginRequest request = new AdminLoginRequest(TEST_ADMIN_LOGIN_ID, TEST_PASSWORD);
        Admin admin = createAdmin();
        TokenResponse expectedToken = new TokenResponse("access-token", "refresh-token");

        given(queryAdminPort.findByAdminId(TEST_ADMIN_LOGIN_ID)).willReturn(Optional.of(admin));
        given(passwordEncoder.matches(TEST_PASSWORD, ENCODED_PASSWORD)).willReturn(true);
        given(jwtTokenProvider.generateToken(TEST_ADMIN_ID, Role.ADMIN.name())).willReturn(expectedToken);
        given(jwtProperties.refreshExpiration()).willReturn(REFRESH_EXPIRATION);

        // when
        adminLoginService.execute(request);

        // then
        ArgumentCaptor<RefreshToken> tokenCaptor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(tokenCaptor.capture());

        RefreshToken savedToken = tokenCaptor.getValue();
        assertThat(savedToken.getId()).isEqualTo(TEST_ADMIN_ID);
        assertThat(savedToken.getToken()).isEqualTo("refresh-token");
        assertThat(savedToken.getTtl()).isEqualTo(REFRESH_EXPIRATION);
    }

    @Test
    @DisplayName("존재하지 않는 관리자 ID로 로그인 시 예외가 발생한다")
    void whenAdminNotFound_thenThrowsException() {
        // given
        AdminLoginRequest request = new AdminLoginRequest("nonexistent", TEST_PASSWORD);

        given(queryAdminPort.findByAdminId("nonexistent")).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> adminLoginService.execute(request))
                .isInstanceOf(AdminNotFoundException.class);
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 예외가 발생한다")
    void whenPasswordMismatch_thenThrowsException() {
        // given
        AdminLoginRequest request = new AdminLoginRequest(TEST_ADMIN_LOGIN_ID, "wrongpassword");
        Admin admin = createAdmin();

        given(queryAdminPort.findByAdminId(TEST_ADMIN_LOGIN_ID)).willReturn(Optional.of(admin));
        given(passwordEncoder.matches("wrongpassword", ENCODED_PASSWORD)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> adminLoginService.execute(request))
                .isInstanceOf(PasswordMissMatchException.class);
    }

    @Test
    @DisplayName("JWT 토큰 생성 시 ADMIN 역할을 사용한다")
    void whenGenerateToken_thenUsesAdminRole() {
        // given
        AdminLoginRequest request = new AdminLoginRequest(TEST_ADMIN_LOGIN_ID, TEST_PASSWORD);
        Admin admin = createAdmin();
        TokenResponse expectedToken = new TokenResponse("access-token", "refresh-token");

        given(queryAdminPort.findByAdminId(TEST_ADMIN_LOGIN_ID)).willReturn(Optional.of(admin));
        given(passwordEncoder.matches(TEST_PASSWORD, ENCODED_PASSWORD)).willReturn(true);
        given(jwtTokenProvider.generateToken(TEST_ADMIN_ID, Role.ADMIN.name())).willReturn(expectedToken);
        given(jwtProperties.refreshExpiration()).willReturn(REFRESH_EXPIRATION);

        // when
        adminLoginService.execute(request);

        // then
        verify(jwtTokenProvider).generateToken(TEST_ADMIN_ID, Role.ADMIN.name());
    }

    @Test
    @DisplayName("리프레시 토큰의 만료 시간은 JwtProperties에서 가져온다")
    void whenSaveRefreshToken_thenUsesExpirationFromProperties() {
        // given
        AdminLoginRequest request = new AdminLoginRequest(TEST_ADMIN_LOGIN_ID, TEST_PASSWORD);
        Admin admin = createAdmin();
        TokenResponse expectedToken = new TokenResponse("access-token", "refresh-token");
        Long customExpiration = 1000000L;

        given(queryAdminPort.findByAdminId(TEST_ADMIN_LOGIN_ID)).willReturn(Optional.of(admin));
        given(passwordEncoder.matches(TEST_PASSWORD, ENCODED_PASSWORD)).willReturn(true);
        given(jwtTokenProvider.generateToken(TEST_ADMIN_ID, Role.ADMIN.name())).willReturn(expectedToken);
        given(jwtProperties.refreshExpiration()).willReturn(customExpiration);

        // when
        adminLoginService.execute(request);

        // then
        ArgumentCaptor<RefreshToken> tokenCaptor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(tokenCaptor.capture());

        RefreshToken savedToken = tokenCaptor.getValue();
        assertThat(savedToken.getTtl()).isEqualTo(customExpiration);
    }

    @Test
    @DisplayName("비밀번호 검증 전에 관리자 조회가 먼저 수행된다")
    void whenLogin_thenQueryAdminBeforePasswordCheck() {
        // given
        AdminLoginRequest request = new AdminLoginRequest("nonexistent", TEST_PASSWORD);

        given(queryAdminPort.findByAdminId("nonexistent")).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> adminLoginService.execute(request))
                .isInstanceOf(AdminNotFoundException.class);

        // 비밀번호 검증은 수행되지 않아야 함
        verify(passwordEncoder, org.mockito.Mockito.never()).matches(any(), any());
    }

    /**
     * 테스트용 Admin 객체를 생성합니다.
     *
     * @return 기본 정보가 설정된 Admin 객체
     */
    private Admin createAdmin() {
        return new Admin(
                TEST_ADMIN_ID,
                TEST_ADMIN_LOGIN_ID,
                ENCODED_PASSWORD
        );
    }
}
