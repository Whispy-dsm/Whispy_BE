package whispy_server.whispy.domain.user.application.service.unit;
import whispy_server.whispy.domain.user.application.service.UserWithdrawalService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.focussession.application.port.out.DeleteFocusSessionPort;
import whispy_server.whispy.domain.history.application.port.out.DeleteListeningHistoryPort;
import whispy_server.whispy.domain.like.application.port.out.DeleteMusicLikePort;
import whispy_server.whispy.domain.meditationsession.application.port.out.DeleteMeditationSessionPort;
import whispy_server.whispy.domain.notification.application.port.out.DeleteNotificationPort;
import whispy_server.whispy.domain.sleepsession.application.port.out.DeleteSleepSessionPort;
import whispy_server.whispy.domain.soundspace.application.port.out.DeleteSoundSpaceMusicPort;
import whispy_server.whispy.domain.topic.application.port.out.DeleteTopicSubscriptionPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.application.port.out.UserDeletePort;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.global.security.jwt.domain.repository.RefreshTokenRepository;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * UserWithdrawalService의 단위 테스트 클래스
 *
 * 회원 탈퇴 서비스의 다양한 시나리오를 검증합니다.
 * 사용자와 관련된 모든 데이터의 삭제를 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserWithdrawalService 테스트")
class UserWithdrawalServiceTest {

    @InjectMocks
    private UserWithdrawalService userWithdrawalService;

    @Mock
    private UserDeletePort userDeletePort;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    @Mock
    private DeleteFocusSessionPort deleteFocusSessionPort;

    @Mock
    private DeleteSleepSessionPort deleteSleepSessionPort;

    @Mock
    private DeleteMeditationSessionPort deleteMeditationSessionPort;

    @Mock
    private DeleteMusicLikePort deleteMusicLikePort;

    @Mock
    private DeleteListeningHistoryPort deleteListeningHistoryPort;

    @Mock
    private DeleteSoundSpaceMusicPort deleteSoundSpaceMusicPort;

    @Mock
    private DeleteNotificationPort deleteNotificationPort;

    @Mock
    private DeleteTopicSubscriptionPort deleteTopicSubscriptionPort;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;

    @Test
    @DisplayName("회원 탈퇴 시 모든 세션 데이터를 삭제한다")
    void whenWithdrawing_thenDeletesAllSessionData() {
        // given
        User user = createUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);

        // when
        userWithdrawalService.withdrawal();

        // then
        verify(deleteFocusSessionPort).deleteByUserId(TEST_USER_ID);
        verify(deleteSleepSessionPort).deleteByUserId(TEST_USER_ID);
        verify(deleteMeditationSessionPort).deleteByUserId(TEST_USER_ID);
    }

    @Test
    @DisplayName("회원 탈퇴 시 좋아요와 히스토리를 삭제한다")
    void whenWithdrawing_thenDeletesLikesAndHistory() {
        // given
        User user = createUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);

        // when
        userWithdrawalService.withdrawal();

        // then
        verify(deleteMusicLikePort).deleteByUserId(TEST_USER_ID);
        verify(deleteListeningHistoryPort).deleteByUserId(TEST_USER_ID);
        verify(deleteSoundSpaceMusicPort).deleteByUserId(TEST_USER_ID);
    }

    @Test
    @DisplayName("회원 탈퇴 시 알림과 토픽 구독을 삭제한다")
    void whenWithdrawing_thenDeletesNotificationsAndTopics() {
        // given
        User user = createUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);

        // when
        userWithdrawalService.withdrawal();

        // then
        verify(deleteNotificationPort).deleteByEmail(TEST_EMAIL);
        verify(deleteTopicSubscriptionPort).deleteByEmail(TEST_EMAIL);
    }

    @Test
    @DisplayName("회원 탈퇴 시 Refresh Token을 삭제한다")
    void whenWithdrawing_thenDeletesRefreshToken() {
        // given
        User user = createUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);

        // when
        userWithdrawalService.withdrawal();

        // then
        verify(refreshTokenRepository).deleteById(TEST_USER_ID);
    }

    @Test
    @DisplayName("회원 탈퇴 시 사용자 계정을 삭제한다")
    void whenWithdrawing_thenDeletesUserAccount() {
        // given
        User user = createUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);

        // when
        userWithdrawalService.withdrawal();

        // then
        verify(userDeletePort).deleteById(TEST_USER_ID);
    }

    @Test
    @DisplayName("회원 탈퇴 시 모든 관련 데이터를 올바른 순서로 삭제한다")
    void whenWithdrawing_thenDeletesAllDataInCorrectOrder() {
        // given
        User user = createUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);

        // when
        userWithdrawalService.withdrawal();

        // then
        // userId 기반 삭제
        verify(deleteFocusSessionPort).deleteByUserId(TEST_USER_ID);
        verify(deleteSleepSessionPort).deleteByUserId(TEST_USER_ID);
        verify(deleteMeditationSessionPort).deleteByUserId(TEST_USER_ID);
        verify(deleteMusicLikePort).deleteByUserId(TEST_USER_ID);
        verify(deleteListeningHistoryPort).deleteByUserId(TEST_USER_ID);
        verify(deleteSoundSpaceMusicPort).deleteByUserId(TEST_USER_ID);

        // email 기반 삭제
        verify(deleteNotificationPort).deleteByEmail(TEST_EMAIL);
        verify(deleteTopicSubscriptionPort).deleteByEmail(TEST_EMAIL);

        // User 삭제
        verify(refreshTokenRepository).deleteById(TEST_USER_ID);
        verify(userDeletePort).deleteById(TEST_USER_ID);
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
