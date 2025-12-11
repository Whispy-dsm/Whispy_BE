package whispy_server.whispy.domain.topic.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.TopicSubscriptionRequest;
import whispy_server.whispy.domain.topic.application.port.out.QueryTopicSubscriptionPort;
import whispy_server.whispy.domain.topic.application.port.out.SaveTopicSubscriptionPort;
import whispy_server.whispy.domain.topic.application.service.SubscriptionTopicService;
import whispy_server.whispy.domain.topic.model.TopicSubscription;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * SubscriptionTopicService의 단위 테스트 클래스
 * <p>
 * 토픽 구독 서비스를 검증합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SubscriptionTopicService 테스트")
class SubscriptionTopicServiceTest {

    @InjectMocks
    private SubscriptionTopicService service;

    @Mock
    private QueryTopicSubscriptionPort queryTopicSubscriptionPort;

    @Mock
    private SaveTopicSubscriptionPort saveTopicSubscriptionPort;

    @Mock
    private FcmSendPort fcmSendPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    @Test
    @DisplayName("토픽을 구독할 수 있다")
    void execute() {
        User user = new User(1L, "test@test.com", "pw", "name", null, Gender.MALE, Role.USER, "fcm-token", null, LocalDateTime.now());
        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryTopicSubscriptionPort.findByEmailAndTopic("test@test.com", NotificationTopic.GENERAL_ANNOUNCEMENT))
                .willReturn(Optional.of(new TopicSubscription(1L, "test@test.com", NotificationTopic.GENERAL_ANNOUNCEMENT, false)));

        service.execute(new TopicSubscriptionRequest(NotificationTopic.GENERAL_ANNOUNCEMENT));

        verify(saveTopicSubscriptionPort).save(any());
    }
}
