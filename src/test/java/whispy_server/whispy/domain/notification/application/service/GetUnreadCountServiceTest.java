package whispy_server.whispy.domain.notification.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.response.UnreadCountResponse;
import whispy_server.whispy.domain.notification.application.port.out.QueryNotificationPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * GetUnreadCountService의 단위 테스트 클래스
 * <p>
 * 읽지 않은 알림 개수 조회 서비스의 다양한 시나리오를 검증합니다.
 * 읽지 않은 알림 개수 계산을 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetUnreadCountService 테스트")
class GetUnreadCountServiceTest {

    @InjectMocks
    private GetUnreadCountService getUnreadCountService;

    @Mock
    private QueryNotificationPort queryNotificationPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;

    @Test
    @DisplayName("읽지 않은 알림이 없는 경우 0을 반환한다")
    void whenNoUnreadNotifications_thenReturnsZero() {
        // given
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryNotificationPort.countByEmailAndIsReadFalse(TEST_EMAIL))
                .willReturn(0);

        // when
        UnreadCountResponse response = getUnreadCountService.execute();

        // then
        assertThat(response.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("읽지 않은 알림이 있는 경우 정확한 개수를 반환한다")
    void whenUnreadNotificationsExist_thenReturnsCorrectCount() {
        // given
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryNotificationPort.countByEmailAndIsReadFalse(TEST_EMAIL))
                .willReturn(5);

        // when
        UnreadCountResponse response = getUnreadCountService.execute();

        // then
        assertThat(response.count()).isEqualTo(5);
    }

    @Test
    @DisplayName("읽지 않은 알림이 많은 경우에도 정확한 개수를 반환한다")
    void whenManyUnreadNotifications_thenReturnsCorrectCount() {
        // given
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryNotificationPort.countByEmailAndIsReadFalse(TEST_EMAIL))
                .willReturn(99);

        // when
        UnreadCountResponse response = getUnreadCountService.execute();

        // then
        assertThat(response.count()).isEqualTo(99);
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
                null,
                Gender.MALE,
                Role.USER,
                null,
                null,
                LocalDateTime.now()
        );
    }
}
