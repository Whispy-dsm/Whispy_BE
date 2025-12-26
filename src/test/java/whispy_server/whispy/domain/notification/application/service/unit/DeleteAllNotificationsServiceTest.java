package whispy_server.whispy.domain.notification.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.notification.application.port.out.DeleteNotificationPort;
import whispy_server.whispy.domain.notification.application.service.DeleteAllNotificationsService;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * DeleteAllNotificationsService의 단위 테스트 클래스
 *
 * 모든 알림 삭제 서비스를 검증합니다.
 * 현재 사용자의 모든 알림이 삭제되는지 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteAllNotificationsService 테스트")
class DeleteAllNotificationsServiceTest {

    @InjectMocks
    private DeleteAllNotificationsService service;

    @Mock
    private DeleteNotificationPort deleteNotificationPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;

    @Test
    @DisplayName("현재 사용자의 모든 알림을 삭제할 수 있다")
    void execute_deletesAllNotifications() {
        // given
        User user = createUser();
        given(userFacadeUseCase.currentUser()).willReturn(user);

        // when
        service.execute();

        // then
        verify(deleteNotificationPort).deleteByEmail(TEST_EMAIL);
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
                "fcm-token",
                null,
                LocalDateTime.now()
        );
    }
}
