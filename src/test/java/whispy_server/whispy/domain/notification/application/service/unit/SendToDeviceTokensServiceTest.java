package whispy_server.whispy.domain.notification.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationSendRequest;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.notification.application.service.SendToDeviceTokensService;
import whispy_server.whispy.domain.notification.application.service.component.NotificationPersister;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

/**
 * SendToDeviceTokensService의 단위 테스트 클래스
 * <p>
 * 디바이스 토큰으로 알림 전송 서비스를 검증합니다.
 * FCM 전송 및 알림 이력 저장 로직을 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SendToDeviceTokensService 테스트")
class SendToDeviceTokensServiceTest {

    @InjectMocks
    private SendToDeviceTokensService service;

    @Mock
    private FcmSendPort fcmSendPort;

    @Mock
    private NotificationPersister notificationPersister;

    private static final String TEST_EMAIL = "test@example.com";

    @Test
    @DisplayName("디바이스 토큰으로 알림을 전송할 수 있다")
    void execute_sendsNotificationToDeviceTokens() {
        // given
        List<String> deviceTokens = Arrays.asList("token1", "token2", "token3");
        Map<String, String> data = new HashMap<>();
        data.put("key", "value");

        NotificationSendRequest request = new NotificationSendRequest(
                TEST_EMAIL,
                deviceTokens,
                NotificationTopic.GENERAL_ANNOUNCEMENT,
                "테스트 제목",
                "테스트 내용",
                data
        );

        // when
        service.execute(request);

        // then
        verify(fcmSendPort).sendMulticast(
                eq(deviceTokens),
                eq("테스트 제목"),
                eq("테스트 내용"),
                eq(data)
        );
    }

    @Test
    @DisplayName("알림 전송 후 이력을 저장한다")
    void execute_savesNotificationHistory() {
        // given
        List<String> deviceTokens = Arrays.asList("token1");
        Map<String, String> data = new HashMap<>();
        data.put("type", "announcement");

        NotificationSendRequest request = new NotificationSendRequest(
                TEST_EMAIL,
                deviceTokens,
                NotificationTopic.GENERAL_ANNOUNCEMENT,
                "제목",
                "내용",
                data
        );

        // when
        service.execute(request);

        // then
        verify(notificationPersister).save(argThat(notification ->
                notification.email().equals(TEST_EMAIL) &&
                        notification.title().equals("제목") &&
                        notification.body().equals("내용") &&
                        notification.topic() == NotificationTopic.GENERAL_ANNOUNCEMENT &&
                        notification.data().equals(data) &&
                        !notification.read()
        ));
    }

    @Test
    @DisplayName("데이터 없이 알림을 전송할 수 있다")
    void execute_sendsNotificationWithoutData() {
        // given
        List<String> deviceTokens = Arrays.asList("token1");

        NotificationSendRequest request = new NotificationSendRequest(
                TEST_EMAIL,
                deviceTokens,
                NotificationTopic.SYSTEM_ANNOUNCEMENT,
                "제목",
                "내용",
                null
        );

        // when
        service.execute(request);

        // then
        verify(fcmSendPort).sendMulticast(
                eq(deviceTokens),
                eq("제목"),
                eq("내용"),
                eq(null)
        );
        verify(notificationPersister).save(any());
    }

    @Test
    @DisplayName("여러 디바이스 토큰으로 알림을 전송할 수 있다")
    void execute_sendsToMultipleDeviceTokens() {
        // given
        List<String> deviceTokens = Arrays.asList(
                "token1",
                "token2",
                "token3",
                "token4",
                "token5"
        );

        NotificationSendRequest request = new NotificationSendRequest(
                TEST_EMAIL,
                deviceTokens,
                NotificationTopic.GENERAL_ANNOUNCEMENT,
                "제목",
                "내용",
                null
        );

        // when
        service.execute(request);

        // then
        verify(fcmSendPort).sendMulticast(
                argThat(tokens -> tokens.size() == 5),
                eq("제목"),
                eq("내용"),
                eq(null)
        );
    }

    @Test
    @DisplayName("알림 이력 저장 시 읽음 여부는 false로 설정된다")
    void execute_savesNotificationAsUnread() {
        // given
        List<String> deviceTokens = Arrays.asList("token1");

        NotificationSendRequest request = new NotificationSendRequest(
                TEST_EMAIL,
                deviceTokens,
                NotificationTopic.GENERAL_ANNOUNCEMENT,
                "제목",
                "내용",
                null
        );

        // when
        service.execute(request);

        // then
        verify(notificationPersister).save(argThat(notification ->
                !notification.read()
        ));
    }
}
