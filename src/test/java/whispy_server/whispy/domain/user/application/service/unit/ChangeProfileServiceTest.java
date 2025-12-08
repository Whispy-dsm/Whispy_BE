package whispy_server.whispy.domain.user.application.service.unit;
import whispy_server.whispy.domain.user.application.service.ChangeProfileService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.ChangeProfileRequest;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * ChangeProfileService의 단위 테스트 클래스
 * <p>
 * 프로필 변경 서비스의 다양한 시나리오를 검증합니다.
 * 사용자 이름, 프로필 이미지, 성별 변경을 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ChangeProfileService 테스트")
class ChangeProfileServiceTest {

    @InjectMocks
    private ChangeProfileService changeProfileService;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    @Mock
    private UserSavePort userSavePort;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;

    @Test
    @DisplayName("프로필 정보를 성공적으로 변경한다")
    void whenChangingProfile_thenUpdatesSuccessfully() {
        // given
        User user = createUser();
        ChangeProfileRequest request = new ChangeProfileRequest(
                "NewName",
                "https://example.com/new-profile.jpg",
                Gender.FEMALE
        );

        given(userFacadeUseCase.currentUser()).willReturn(user);

        // when
        changeProfileService.execute(request);

        // then
        verify(userSavePort).save(any(User.class));
    }

    @Test
    @DisplayName("이름을 변경할 수 있다")
    void whenChangingNameOnly_thenUpdatesName() {
        // given
        User user = createUser();
        ChangeProfileRequest request = new ChangeProfileRequest(
                "NewName",
                user.profileImageUrl(),
                user.gender()
        );

        given(userFacadeUseCase.currentUser()).willReturn(user);

        // when
        changeProfileService.execute(request);

        // then
        verify(userSavePort).save(any(User.class));
    }

    @Test
    @DisplayName("프로필 이미지를 변경할 수 있다")
    void whenChangingProfileImageOnly_thenUpdatesImage() {
        // given
        User user = createUser();
        ChangeProfileRequest request = new ChangeProfileRequest(
                user.name(),
                "https://example.com/new-image.jpg",
                user.gender()
        );

        given(userFacadeUseCase.currentUser()).willReturn(user);

        // when
        changeProfileService.execute(request);

        // then
        verify(userSavePort).save(any(User.class));
    }

    @Test
    @DisplayName("성별을 변경할 수 있다")
    void whenChangingGenderOnly_thenUpdatesGender() {
        // given
        User user = createUser();
        ChangeProfileRequest request = new ChangeProfileRequest(
                user.name(),
                user.profileImageUrl(),
                Gender.FEMALE
        );

        given(userFacadeUseCase.currentUser()).willReturn(user);

        // when
        changeProfileService.execute(request);

        // then
        verify(userSavePort).save(any(User.class));
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
                null,
                LocalDateTime.now()
        );
    }
}
