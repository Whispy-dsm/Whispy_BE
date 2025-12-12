package whispy_server.whispy.domain.notification.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationTopicSendRequest;
import whispy_server.whispy.domain.notification.application.port.in.SendToTopicUseCase;
import whispy_server.whispy.domain.notification.application.service.BroadCastAllUsersService;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

/**
 * BroadCastAllUsersService의 단위 테스트 클래스
 * <p>
 * 모든 사용자에게 알림 전송 서비스를 검증합니다.
 * BROADCAST_ANNOUNCEMENT 토픽으로 변환되어 전송되는지 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BroadCastAllUsersService 테스트")
class BroadCastAllUsersServiceTest {

    @InjectMocks
    private BroadCastAllUsersService service;

    @Mock
    private SendToTopicUseCase sendToTopicUseCase;

    @Test
    @DisplayName("모든 사용자에게 알림을 전송할 수 있다")
    void execute_broadcastsToAllUsers() {
        // given
        Map<String, String> data = new HashMap<>();
        data.put("key", "value");

        NotificationTopicSendRequest request = new NotificationTopicSendRequest(
                NotificationTopic.GENERAL_ANNOUNCEMENT,
                "공지사항 제목",
                "공지사항 내용",
                data
        );

        // when
        service.execute(request);

        // then
        verify(sendToTopicUseCase).execute(argThat(arg ->
                arg.topic() == NotificationTopic.BROADCAST_ANNOUNCEMENT &&
                        arg.title().equals("공지사항 제목") &&
                        arg.body().equals("공지사항 내용") &&
                        arg.data().equals(data)
        ));
    }

    @Test
    @DisplayName("데이터 없이 모든 사용자에게 알림을 전송할 수 있다")
    void execute_broadcastsWithoutData() {
        // given
        NotificationTopicSendRequest request = new NotificationTopicSendRequest(
                NotificationTopic.GENERAL_ANNOUNCEMENT,
                "제목",
                "내용",
                null
        );

        // when
        service.execute(request);

        // then
        verify(sendToTopicUseCase).execute(any(NotificationTopicSendRequest.class));
    }

    @Test
    @DisplayName("입력 토픽과 무관하게 BROADCAST_ANNOUNCEMENT 토픽으로 전송된다")
    void execute_alwaysUsesBroadcastTopic() {
        // given
        NotificationTopicSendRequest request = new NotificationTopicSendRequest(
                NotificationTopic.SYSTEM_ANNOUNCEMENT,
                "제목",
                "내용",
                null
        );

        // when
        service.execute(request);

        // then
        verify(sendToTopicUseCase).execute(argThat(arg ->
                arg.topic() == NotificationTopic.BROADCAST_ANNOUNCEMENT
        ));
    }
}
