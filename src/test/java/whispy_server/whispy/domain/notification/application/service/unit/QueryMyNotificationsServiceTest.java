package whispy_server.whispy.domain.notification.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.response.NotificationResponse;
import whispy_server.whispy.domain.notification.application.port.out.QueryNotificationPort;
import whispy_server.whispy.domain.notification.application.service.QueryMyNotificationsService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * QueryMyNotificationsService의 단위 테스트 클래스
 * <p>
 * 내 알림 목록 조회 서비스를 검증합니다.
 * 페이지네이션 및 알림 목록 조회 로직을 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("QueryMyNotificationsService 테스트")
class QueryMyNotificationsServiceTest {

    @InjectMocks
    private QueryMyNotificationsService service;

    @Mock
    private QueryNotificationPort queryNotificationPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;

    @Test
    @DisplayName("내 알림 목록을 페이지네이션하여 조회할 수 있다")
    void execute_returnsPagedNotifications() {
        // given
        User user = createUser();
        Pageable pageable = PageRequest.of(0, 10);
        List<Notification> notifications = Arrays.asList(
                createNotification(1L, false),
                createNotification(2L, true),
                createNotification(3L, false)
        );
        Page<Notification> notificationPage = new PageImpl<>(notifications, pageable, notifications.size());

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryNotificationPort.findByEmailOrderByCreatedAtDesc(TEST_EMAIL, pageable))
                .willReturn(notificationPage);

        // when
        Page<NotificationResponse> result = service.execute(pageable);

        // then
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getContent().get(0).id()).isEqualTo(1L);
        assertThat(result.getContent().get(1).id()).isEqualTo(2L);
        assertThat(result.getContent().get(2).id()).isEqualTo(3L);
    }

    @Test
    @DisplayName("알림이 없으면 빈 페이지를 반환한다")
    void execute_returnsEmptyPage_whenNoNotifications() {
        // given
        User user = createUser();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Notification> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryNotificationPort.findByEmailOrderByCreatedAtDesc(TEST_EMAIL, pageable))
                .willReturn(emptyPage);

        // when
        Page<NotificationResponse> result = service.execute(pageable);

        // then
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("두 번째 페이지를 조회할 수 있다")
    void execute_returnsSecondPage() {
        // given
        User user = createUser();
        Pageable pageable = PageRequest.of(1, 2);
        List<Notification> notifications = Arrays.asList(
                createNotification(3L, false),
                createNotification(4L, false)
        );
        Page<Notification> notificationPage = new PageImpl<>(notifications, pageable, 5);

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryNotificationPort.findByEmailOrderByCreatedAtDesc(TEST_EMAIL, pageable))
                .willReturn(notificationPage);

        // when
        Page<NotificationResponse> result = service.execute(pageable);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(5);
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getContent().get(0).id()).isEqualTo(3L);
        assertThat(result.getContent().get(1).id()).isEqualTo(4L);
    }

    @Test
    @DisplayName("NotificationResponse로 올바르게 변환된다")
    void execute_convertsToNotificationResponse() {
        // given
        User user = createUser();
        Pageable pageable = PageRequest.of(0, 1);
        Notification notification = createNotification(1L, false);
        Page<Notification> notificationPage = new PageImpl<>(List.of(notification), pageable, 1);

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryNotificationPort.findByEmailOrderByCreatedAtDesc(TEST_EMAIL, pageable))
                .willReturn(notificationPage);

        // when
        Page<NotificationResponse> result = service.execute(pageable);

        // then
        NotificationResponse response = result.getContent().get(0);
        assertThat(response.id()).isEqualTo(notification.id());
        assertThat(response.email()).isEqualTo(notification.email());
        assertThat(response.title()).isEqualTo(notification.title());
        assertThat(response.body()).isEqualTo(notification.body());
        assertThat(response.topic()).isEqualTo(notification.topic());
        assertThat(response.read()).isEqualTo(notification.read());
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
     * @param id   알림 ID
     * @param read 읽음 여부
     * @return 생성된 Notification 객체
     */
    private Notification createNotification(Long id, boolean read) {
        return new Notification(
                id,
                TEST_EMAIL,
                "테스트 제목 " + id,
                "테스트 내용 " + id,
                NotificationTopic.GENERAL_ANNOUNCEMENT,
                null,
                read,
                LocalDateTime.now().minusDays(id)
        );
    }
}
