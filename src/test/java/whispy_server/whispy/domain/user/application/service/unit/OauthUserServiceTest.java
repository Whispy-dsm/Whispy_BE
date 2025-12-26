package whispy_server.whispy.domain.user.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.user.application.port.out.QueryUserPort;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.domain.user.application.service.OauthUserService;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.oauth.dto.OauthUserInfo;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * OauthUserService의 단위 테스트 클래스
 *
 * OAuth 사용자 처리 서비스의 다양한 시나리오를 검증합니다.
 * 기존 사용자 조회 및 신규 사용자 생성을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("OauthUserService 테스트")
class OauthUserServiceTest {

    @InjectMocks
    private OauthUserService oauthUserService;

    @Mock
    private QueryUserPort queryUserPort;

    @Mock
    private UserSavePort userSavePort;

    private static final String TEST_EMAIL = "oauth@example.com";
    private static final String TEST_NAME = "OauthUser";
    private static final String TEST_PROFILE_IMAGE = "https://example.com/profile.jpg";
    private static final Long TEST_USER_ID = 1L;

    @Test
    @DisplayName("기존 사용자가 존재하면 해당 사용자를 반환한다")
    void whenUserExists_thenReturnExistingUser() {
        // given
        OauthUserInfo oauthUserInfo = new OauthUserInfo(TEST_NAME, TEST_EMAIL, TEST_PROFILE_IMAGE);
        User existingUser = createExistingUser();

        given(queryUserPort.findByEmail(TEST_EMAIL)).willReturn(Optional.of(existingUser));

        // when
        User result = oauthUserService.findOrCreateOauthUser(oauthUserInfo, "GOOGLE");

        // then
        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo(TEST_EMAIL);
        assertThat(result.id()).isEqualTo(TEST_USER_ID);
        verify(userSavePort, never()).save(any());
    }

    @Test
    @DisplayName("신규 사용자인 경우 새로운 사용자를 생성한다")
    void whenUserNotExists_thenCreateNewUser() {
        // given
        OauthUserInfo oauthUserInfo = new OauthUserInfo(TEST_NAME, TEST_EMAIL, TEST_PROFILE_IMAGE);

        given(queryUserPort.findByEmail(TEST_EMAIL)).willReturn(Optional.empty());

        // when
        User result = oauthUserService.findOrCreateOauthUser(oauthUserInfo, "GOOGLE");

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userSavePort).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.email()).isEqualTo(TEST_EMAIL);
        assertThat(savedUser.name()).isEqualTo(TEST_NAME);
        assertThat(savedUser.profileImageUrl()).isEqualTo(TEST_PROFILE_IMAGE);
        assertThat(savedUser.gender()).isEqualTo(Gender.UNKNOWN);
        assertThat(savedUser.role()).isEqualTo(Role.USER);
        assertThat(savedUser.provider()).isEqualTo("GOOGLE");
        assertThat(savedUser.password()).isNull();
        assertThat(savedUser.fcmToken()).isNull();
    }

    @Test
    @DisplayName("Kakao 제공자로 신규 사용자를 생성할 수 있다")
    void whenKakaoProvider_thenCreateUserWithKakaoType() {
        // given
        OauthUserInfo oauthUserInfo = new OauthUserInfo(TEST_NAME, TEST_EMAIL, TEST_PROFILE_IMAGE);

        given(queryUserPort.findByEmail(TEST_EMAIL)).willReturn(Optional.empty());

        // when
        oauthUserService.findOrCreateOauthUser(oauthUserInfo, "kakao");

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userSavePort).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.provider()).isEqualTo("KAKAO");
    }

    @Test
    @DisplayName("제공자 이름은 대문자로 변환되어 저장된다")
    void whenProviderInLowerCase_thenConvertToUpperCase() {
        // given
        OauthUserInfo oauthUserInfo = new OauthUserInfo(TEST_NAME, TEST_EMAIL, TEST_PROFILE_IMAGE);

        given(queryUserPort.findByEmail(TEST_EMAIL)).willReturn(Optional.empty());

        // when
        oauthUserService.findOrCreateOauthUser(oauthUserInfo, "google");

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userSavePort).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.provider()).isEqualTo("GOOGLE");
    }

    @Test
    @DisplayName("프로필 이미지가 없는 OAuth 사용자도 생성할 수 있다")
    void whenNoProfileImage_thenCreateUserWithoutImage() {
        // given
        OauthUserInfo oauthUserInfo = new OauthUserInfo(TEST_NAME, TEST_EMAIL, null);

        given(queryUserPort.findByEmail(TEST_EMAIL)).willReturn(Optional.empty());

        // when
        oauthUserService.findOrCreateOauthUser(oauthUserInfo, "GOOGLE");

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userSavePort).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.profileImageUrl()).isNull();
    }

    @Test
    @DisplayName("신규 사용자의 성별은 UNKNOWN으로 설정된다")
    void whenCreateNewUser_thenGenderIsUnknown() {
        // given
        OauthUserInfo oauthUserInfo = new OauthUserInfo(TEST_NAME, TEST_EMAIL, TEST_PROFILE_IMAGE);

        given(queryUserPort.findByEmail(TEST_EMAIL)).willReturn(Optional.empty());

        // when
        oauthUserService.findOrCreateOauthUser(oauthUserInfo, "GOOGLE");

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userSavePort).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.gender()).isEqualTo(Gender.UNKNOWN);
    }

    @Test
    @DisplayName("신규 사용자의 역할은 USER로 설정된다")
    void whenCreateNewUser_thenRoleIsUser() {
        // given
        OauthUserInfo oauthUserInfo = new OauthUserInfo(TEST_NAME, TEST_EMAIL, TEST_PROFILE_IMAGE);

        given(queryUserPort.findByEmail(TEST_EMAIL)).willReturn(Optional.empty());

        // when
        oauthUserService.findOrCreateOauthUser(oauthUserInfo, "GOOGLE");

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userSavePort).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.role()).isEqualTo(Role.USER);
    }

    /**
     * 테스트용 기존 사용자 객체를 생성합니다.
     *
     * @return 기본 정보가 설정된 User 객체
     */
    private User createExistingUser() {
        return new User(
                TEST_USER_ID,
                TEST_EMAIL,
                null,
                TEST_NAME,
                TEST_PROFILE_IMAGE,
                Gender.UNKNOWN,
                Role.USER,
                "GOOGLE",
                null,
                LocalDateTime.now()
        );
    }
}
