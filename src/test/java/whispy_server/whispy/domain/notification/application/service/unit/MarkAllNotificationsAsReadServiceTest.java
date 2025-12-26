package whispy_server.whispy.domain.notification.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.notification.application.port.out.QueryNotificationPort;
import whispy_server.whispy.domain.notification.application.port.out.SaveNotificationPort;
import whispy_server.whispy.domain.notification.application.service.MarkAllNotificationsAsReadService;
import whispy_server.whispy.domain.notification.model.Notification;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * MarkAllNotificationsAsReadService의 단위 테스트 클래스
 *
 * 모든 알림 읽음 처리 서비스를 검증합니다.
 * 읽지 않은 알림들이 모두 읽음 상태로 변경되는지 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("MarkAllNotificationsAsReadService 테스트")
class MarkAllNotificationsAsReadServiceTest {

    @InjectMocks
    private MarkAllNotificationsAsReadService service;

    @Mock
    private QueryNotificationPort queryNotificationPort;

    @Mock
    private SaveNotificationPort saveNotificationPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;

    @Test
    @DisplayName("읽지 않은 모든 알림을 읽음 상태로 변경할 수 있다")
    void execute_marksAllNotificationsAsRead() {
        // given
        User user = createUser();
        List<Notification> unreadNotifications = Arrays.asList(
                createUnreadNotification(1L),
                createUnreadNotification(2L),
                createUnreadNotification(3L)
        );

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryNotificationPort.findByEmailAndIsReadFalse(TEST_EMAIL))
                .willReturn(unreadNotifications);

        // when
        service.execute();

        // then
        verify(saveNotificationPort).saveAll(argThat(notifications ->
                notifications.size() == 3 &&
                        notifications.stream().allMatch(Notification::read)
        ));
    }

    @Test
    @DisplayName("읽지 않은 알림이 없으면 빈 리스트를 저장한다")
    void execute_savesEmptyList_whenNoUnreadNotifications() {
        // given
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryNotificationPort.findByEmailAndIsReadFalse(TEST_EMAIL))
                .willReturn(Collections.emptyList());

        // when
        service.execute();

        // then
        verify(saveNotificationPort).saveAll(Collections.emptyList());
    }

    @Test
    @DisplayName("한 개의 읽지 않은 알림도 정상적으로 처리된다")
    void execute_marksSingleNotificationAsRead() {
        // given
        User user = createUser();
        List<Notification> unreadNotifications = List.of(createUnreadNotification(1L));

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryNotificationPort.findByEmailAndIsReadFalse(TEST_EMAIL))
                .willReturn(unreadNotifications);

        // when
        service.execute();

        // then
        verify(saveNotificationPort).saveAll(argThat(notifications ->
                notifications.size() == 1 &&
                        notifications.get(0).read()
        ));
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
     * 테스트용 읽지 않은 Notification 객체를 생성합니다.
     *
     * @param id 알림 ID
     * @return 생성된 Notification 객체
     */
    private Notification createUnreadNotification(Long id) {
        return new Notification(
                id,
                TEST_EMAIL,
                "테스트 제목",
                "테스트 내용",
                NotificationTopic.GENERAL_ANNOUNCEMENT,
                null,
                false,
                LocalDateTime.now()
        );
    }
}
