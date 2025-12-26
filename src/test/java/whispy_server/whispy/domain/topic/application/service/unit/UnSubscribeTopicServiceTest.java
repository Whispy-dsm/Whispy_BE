package whispy_server.whispy.domain.topic.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.TopicSubscriptionRequest;
import whispy_server.whispy.domain.topic.application.service.UnSubscribeTopicService;
import whispy_server.whispy.domain.topic.application.service.component.TopicUnsubscriber;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import whispy_server.whispy.global.exception.domain.fcm.TopicSubscriptionNotFoundException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

/**
 * UnSubscribeTopicService의 단위 테스트 클래스
 *
 * 토픽 구독 취소 서비스를 검증합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UnSubscribeTopicService 테스트")
class UnSubscribeTopicServiceTest {

    @InjectMocks
    private UnSubscribeTopicService service;

    @Mock
    private FcmSendPort fcmSendPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    @Mock
    private TopicUnsubscriber topicUnsubscriber;

    @Test
    @DisplayName("토픽 구독을 취소할 수 있다")
    void execute() {
        User user = new User(1L, "test@test.com", "pw", "name", null, Gender.MALE, Role.USER, null, "fcm-token", LocalDateTime.now());

        given(userFacadeUseCase.currentUser()).willReturn(user);

        service.execute(new TopicSubscriptionRequest(NotificationTopic.GENERAL_ANNOUNCEMENT));

        verify(topicUnsubscriber).unsubscribe("test@test.com", NotificationTopic.GENERAL_ANNOUNCEMENT);
        verify(fcmSendPort).unsubscribeFromTopic("fcm-token", NotificationTopic.GENERAL_ANNOUNCEMENT);
    }

    @Test
    @DisplayName("구독 정보가 존재하지 않으면 TopicSubscriptionNotFoundException이 발생한다")
    void execute_throwsException_whenSubscriptionNotFound() {
        // given
        User user = new User(1L, "test@test.com", "pw", "name", null, Gender.MALE, Role.USER, null, "fcm-token", LocalDateTime.now());
        TopicSubscriptionRequest request = new TopicSubscriptionRequest(NotificationTopic.GENERAL_ANNOUNCEMENT);

        given(userFacadeUseCase.currentUser()).willReturn(user);
        doThrow(TopicSubscriptionNotFoundException.EXCEPTION)
                .when(topicUnsubscriber).unsubscribe("test@test.com", NotificationTopic.GENERAL_ANNOUNCEMENT);

        // when & then
        assertThrows(TopicSubscriptionNotFoundException.class, () -> service.execute(request));
    }
}
