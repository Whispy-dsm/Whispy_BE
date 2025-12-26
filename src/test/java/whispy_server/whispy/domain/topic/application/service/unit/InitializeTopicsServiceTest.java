package whispy_server.whispy.domain.topic.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.topic.application.port.out.QueryTopicSubscriptionPort;
import whispy_server.whispy.domain.topic.application.service.InitializeTopicsService;
import whispy_server.whispy.domain.topic.application.service.component.TopicInitializer;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * InitializeTopicsService의 단위 테스트 클래스
 *
 * 토픽 초기화 서비스를 검증합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("InitializeTopicsService 테스트")
class InitializeTopicsServiceTest {

    @InjectMocks
    private InitializeTopicsService service;

    @Mock
    private QueryTopicSubscriptionPort queryTopicSubscriptionPort;

    @Mock
    private FcmSendPort fcmSendPort;

    @Mock
    private TopicInitializer topicInitializer;

    @Test
    @DisplayName("신규 사용자의 토픽을 초기화할 수 있다")
    void execute() {
        // given
        String email = "test@test.com";
        String fcmToken = "fcm-token";
        given(queryTopicSubscriptionPort.findByEmail(email)).willReturn(List.of());

        // when
        service.execute(email, fcmToken, true);

        // then
        verify(queryTopicSubscriptionPort).findByEmail(email);
        verify(topicInitializer).createAllTopicsForNewUser(email, true);
        verify(fcmSendPort, atLeastOnce()).subscribeToTopic(any(String.class), any());
    }
}
