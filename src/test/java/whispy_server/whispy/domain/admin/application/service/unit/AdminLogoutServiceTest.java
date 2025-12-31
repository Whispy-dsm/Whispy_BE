package whispy_server.whispy.domain.admin.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.admin.application.port.in.AdminFacadeUseCase;
import whispy_server.whispy.domain.admin.application.service.AdminLogoutService;
import whispy_server.whispy.domain.admin.model.Admin;
import whispy_server.whispy.global.security.jwt.domain.repository.RefreshTokenRepository;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * AdminLogoutService의 단위 테스트 클래스
 *
 * 관리자 로그아웃 서비스의 동작을 검증합니다.
 * RefreshToken 삭제를 테스트합니다.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AdminLogoutService 테스트")
class AdminLogoutServiceTest {

    @InjectMocks
    private AdminLogoutService adminLogoutService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private AdminFacadeUseCase adminFacadeUseCase;

    private static final Long TEST_ADMIN_ID = 1L;
    private static final String TEST_ADMIN_LOGIN_ID = "admin";

    @Test
    @DisplayName("로그아웃 시 RefreshToken을 삭제한다")
    void whenLogout_thenDeletesRefreshToken() {
        // given
        Admin admin = createAdmin();
        given(adminFacadeUseCase.currentAdmin()).willReturn(admin);

        // when
        adminLogoutService.execute();

        // then
        verify(refreshTokenRepository).deleteById(TEST_ADMIN_ID);
    }

    @Test
    @DisplayName("현재 관리자의 ID로 RefreshToken을 삭제한다")
    void whenLogout_thenUsesCurrentAdminId() {
        // given
        Admin admin = createAdmin();
        given(adminFacadeUseCase.currentAdmin()).willReturn(admin);

        // when
        adminLogoutService.execute();

        // then
        verify(adminFacadeUseCase).currentAdmin();
        verify(refreshTokenRepository).deleteById(TEST_ADMIN_ID);
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
                "encoded_password"
        );
    }
}
