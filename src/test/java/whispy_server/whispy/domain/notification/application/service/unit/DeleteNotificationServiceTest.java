package whispy_server.whispy.domain.notification.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.notification.application.port.out.DeleteNotificationPort;
import whispy_server.whispy.domain.notification.application.port.out.QueryNotificationPort;
import whispy_server.whispy.domain.notification.application.service.DeleteNotificationService;
import whispy_server.whispy.domain.notification.model.Notification;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.exception.domain.fcm.NotificationNotFoundException;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * DeleteNotificationService의 단위 테스트 클래스
 * <p>
 * 알림 삭제 서비스를 검증합니다.
 * 소유권 검증 및 알림 삭제 로직을 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteNotificationService 테스트")
class DeleteNotificationServiceTest {

    @InjectMocks
    private DeleteNotificationService service;

    @Mock
    private QueryNotificationPort queryNotificationPort;

    @Mock
    private DeleteNotificationPort deleteNotificationPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;
    private static final Long NOTIFICATION_ID = 10L;

    @Test
    @DisplayName("본인의 알림을 삭제할 수 있다")
    void execute_deletesOwnNotification() {
        // given
        User user = createUser();
        Notification notification = createNotification(TEST_EMAIL);

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryNotificationPort.findById(NOTIFICATION_ID))
                .willReturn(Optional.of(notification));

        // when
        service.execute(NOTIFICATION_ID);

        // then
        verify(deleteNotificationPort).deleteById(NOTIFICATION_ID);
    }

    @Test
    @DisplayName("알림이 존재하지 않으면 NotificationNotFoundException이 발생한다")
    void execute_throwsException_whenNotificationNotFound() {
        // given
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryNotificationPort.findById(NOTIFICATION_ID))
                .willReturn(Optional.empty());

        // when & then
        assertThrows(NotificationNotFoundException.class,
                () -> service.execute(NOTIFICATION_ID));
    }

    @Test
    @DisplayName("다른 사용자의 알림을 삭제하려 하면 NotificationNotFoundException이 발생한다")
    void execute_throwsException_whenNotOwner() {
        // given
        User user = createUser();
        Notification notification = createNotification("other@example.com");

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryNotificationPort.findById(NOTIFICATION_ID))
                .willReturn(Optional.of(notification));

        // when & then
        assertThrows(NotificationNotFoundException.class,
                () -> service.execute(NOTIFICATION_ID));
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

    /**
     * 테스트용 Notification 객체를 생성합니다.
     *
     * @param email 알림 수신자 이메일
     * @return 생성된 Notification 객체
     */
    private Notification createNotification(String email) {
        return new Notification(
                NOTIFICATION_ID,
                email,
                "테스트 제목",
                "테스트 내용",
                NotificationTopic.GENERAL_ANNOUNCEMENT,
                null,
                false,
                LocalDateTime.now()
        );
    }
}
