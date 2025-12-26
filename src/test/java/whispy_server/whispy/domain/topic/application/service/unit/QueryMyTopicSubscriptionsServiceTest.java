package whispy_server.whispy.domain.topic.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.topic.application.port.out.QueryTopicSubscriptionPort;
import whispy_server.whispy.domain.topic.application.service.QueryMyTopicSubscriptionsService;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * QueryMyTopicSubscriptionsService의 단위 테스트 클래스
 *
 * 내 토픽 구독 목록 조회 서비스를 검증합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("QueryMyTopicSubscriptionsService 테스트")
class QueryMyTopicSubscriptionsServiceTest {

    @InjectMocks
    private QueryMyTopicSubscriptionsService service;

    @Mock
    private QueryTopicSubscriptionPort queryTopicSubscriptionPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    @Test
    @DisplayName("내 토픽 구독 목록을 조회할 수 있다")
    void execute() {
        given(userFacadeUseCase.currentUser()).willReturn(new User(1L, "test@test.com", "pw", "name", null, Gender.MALE, Role.USER, null, null, LocalDateTime.now()));
        given(queryTopicSubscriptionPort.findByEmail("test@test.com")).willReturn(List.of());

        assertThat(service.execute()).isNotNull();
    }
}
