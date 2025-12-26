package whispy_server.whispy.domain.user.application.service.unit;
import whispy_server.whispy.domain.user.application.service.GetMyAccountInfoService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.MyAccountInfoResponse;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * GetMyAccountInfoService의 단위 테스트 클래스
 *
 * 계정 정보 조회 서비스의 다양한 시나리오를 검증합니다.
 * 일반 로그인과 OAuth 로그인 사용자의 비밀번호 마스킹 처리를 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetMyAccountInfoService 테스트")
class GetMyAccountInfoServiceTest {

    @InjectMocks
    private GetMyAccountInfoService getMyAccountInfoService;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_PASSWORD = "password123";

    @Test
    @DisplayName("일반 로그인 사용자의 경우 비밀번호를 마스킹하여 반환한다")
    void whenLocalUser_thenMasksPassword() {
        // given
        User user = createLocalUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);

        // when
        MyAccountInfoResponse response = getMyAccountInfoService.execute();

        // then
        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo(TEST_EMAIL);
        assertThat(response.name()).isEqualTo("TestUser");
        assertThat(response.provider()).isEqualTo("일반 로그인");
        assertThat(response.password()).isEqualTo("***********"); // password123 길이만큼 마스킹
        assertThat(response.password().length()).isEqualTo(TEST_PASSWORD.length());
    }

    @Test
    @DisplayName("OAuth 사용자의 경우 비밀번호를 null로 반환한다")
    void whenOAuthUser_thenPasswordIsNull() {
        // given
        User user = createOAuthUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);

        // when
        MyAccountInfoResponse response = getMyAccountInfoService.execute();

        // then
        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo(TEST_EMAIL);
        assertThat(response.provider()).isEqualTo("KAKAO");
        assertThat(response.password()).isNull();
    }

    @Test
    @DisplayName("사용자의 프로필 정보를 정확히 반환한다")
    void whenGettingAccountInfo_thenReturnsCorrectProfile() {
        // given
        User user = createLocalUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);

        // when
        MyAccountInfoResponse response = getMyAccountInfoService.execute();

        // then
        assertThat(response.name()).isEqualTo("TestUser");
        assertThat(response.profileImageUrl()).isEqualTo("https://example.com/profile.jpg");
        assertThat(response.gender()).isEqualTo(Gender.MALE);
    }

    /**
     * 테스트용 일반 로그인 User 객체를 생성합니다.
     *
     * @return 생성된 User 객체
     */
    private User createLocalUser() {
        return new User(
                TEST_USER_ID,
                TEST_EMAIL,
                TEST_PASSWORD,
                "TestUser",
                "https://example.com/profile.jpg",
                Gender.MALE,
                Role.USER,
                "일반 로그인",
                null,
                LocalDateTime.now()
        );
    }

    /**
     * 테스트용 OAuth User 객체를 생성합니다.
     *
     * @return 생성된 User 객체
     */
    private User createOAuthUser() {
        return new User(
                TEST_USER_ID,
                TEST_EMAIL,
                null, // OAuth 사용자는 비밀번호 없음
                "TestUser",
                "https://example.com/profile.jpg",
                Gender.MALE,
                Role.USER,
                "KAKAO",
                null,
                LocalDateTime.now()
        );
    }
}
