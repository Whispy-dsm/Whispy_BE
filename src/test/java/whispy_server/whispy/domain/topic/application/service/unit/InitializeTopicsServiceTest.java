package whispy_server.whispy.domain.topic.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.topic.application.port.out.QueryTopicSubscriptionPort;
import whispy_server.whispy.domain.topic.application.port.out.SaveTopicSubscriptionPort;
import whispy_server.whispy.domain.topic.application.service.InitializeTopicsService;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * InitializeTopicsService의 단위 테스트 클래스
 * <p>
 * 토픽 초기화 서비스를 검증합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("InitializeTopicsService 테스트")
class InitializeTopicsServiceTest {

    @InjectMocks
    private InitializeTopicsService service;

    @Mock
    private QueryTopicSubscriptionPort queryTopicSubscriptionPort;

    @Mock
    private SaveTopicSubscriptionPort saveTopicSubscriptionPort;

    @Mock
    private FcmSendPort fcmSendPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

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
        verify(saveTopicSubscriptionPort, atLeastOnce()).save(any());
    }
}
