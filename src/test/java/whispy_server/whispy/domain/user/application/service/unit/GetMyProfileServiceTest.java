package whispy_server.whispy.domain.user.application.service.unit;
import whispy_server.whispy.domain.user.application.service.GetMyProfileService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.MyProfileResponse;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * GetMyProfileService의 단위 테스트 클래스
 *
 * 프로필 조회 서비스의 다양한 시나리오를 검증합니다.
 * 사용자 프로필 정보 및 가입 후 경과 일수 계산을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetMyProfileService 테스트")
class GetMyProfileServiceTest {

    @InjectMocks
    private GetMyProfileService getMyProfileService;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;

    @Test
    @DisplayName("사용자 프로필 정보를 정상적으로 반환한다")
    void whenGettingProfile_thenReturnsProfileInfo() {
        // given
        User user = createUser(LocalDateTime.now().minusDays(10));
        given(userFacadeUseCase.currentUser()).willReturn(user);

        // when
        MyProfileResponse response = getMyProfileService.execute();

        // then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("TestUser");
        assertThat(response.profileImageUrl()).isEqualTo("https://example.com/profile.jpg");
    }

    @Test
    @DisplayName("가입 후 경과 일수를 정확히 계산한다")
    void whenGettingProfile_thenCalculatesDaysSinceRegistration() {
        // given
        LocalDateTime createdAt = LocalDateTime.now().minusDays(30);
        User user = createUser(createdAt);
        given(userFacadeUseCase.currentUser()).willReturn(user);

        // when
        MyProfileResponse response = getMyProfileService.execute();

        // then
        assertThat(response.daysSinceRegistration()).isGreaterThanOrEqualTo(30L);
    }

    @Test
    @DisplayName("신규 가입 사용자의 경과 일수는 0이다")
    void whenNewUser_thenDaysSinceRegistrationIsZero() {
        // given
        User user = createUser(LocalDateTime.now());
        given(userFacadeUseCase.currentUser()).willReturn(user);

        // when
        MyProfileResponse response = getMyProfileService.execute();

        // then
        assertThat(response.daysSinceRegistration()).isEqualTo(0L);
    }

    /**
     * 테스트용 User 객체를 생성합니다.
     *
     * @param createdAt 생성 일시
     * @return 생성된 User 객체
     */
    private User createUser(LocalDateTime createdAt) {
        return new User(
                TEST_USER_ID,
                TEST_EMAIL,
                "password",
                "TestUser",
                "https://example.com/profile.jpg",
                Gender.MALE,
                Role.USER,
                "일반 로그인",
                null,
                createdAt
        );
    }
}
