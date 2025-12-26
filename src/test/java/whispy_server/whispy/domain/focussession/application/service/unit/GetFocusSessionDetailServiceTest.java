package whispy_server.whispy.domain.focussession.application.service.unit;
import whispy_server.whispy.domain.focussession.application.service.GetFocusSessionDetailService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.response.FocusSessionDetailResponse;
import whispy_server.whispy.domain.focussession.application.port.out.QueryFocusSessionPort;
import whispy_server.whispy.domain.focussession.model.FocusSession;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.exception.domain.focussession.FocusSessionNotFoundException;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

/**
 * GetFocusSessionDetailService의 단위 테스트 클래스
 *
 * 집중 세션 상세 조회 서비스의 다양한 시나리오를 검증합니다.
 * 세션 조회 및 소유권 검증 로직을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetFocusSessionDetailService 테스트")
class GetFocusSessionDetailServiceTest {

    @InjectMocks
    private GetFocusSessionDetailService getFocusSessionDetailService;

    @Mock
    private QueryFocusSessionPort queryFocusSessionPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_SESSION_ID = 100L;

    @Test
    @DisplayName("존재하는 세션을 조회할 수 있다")
    void whenSessionExists_thenReturnsDetail() {
        // given
        User user = createUser();
        FocusSession session = createFocusSession();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryFocusSessionPort.findByIdAndUserId(TEST_USER_ID, TEST_SESSION_ID))
                .willReturn(Optional.of(session));

        // when
        FocusSessionDetailResponse response = getFocusSessionDetailService.execute(TEST_SESSION_ID);

        // then
        assertThat(response).isNotNull();
        assertThat(response.durationMinutes()).isEqualTo(120);
        assertThat(response.tag()).isEqualTo(FocusTag.WORK);
        assertThat(response.date()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 세션 조회 시 예외가 발생한다")
    void whenSessionNotExists_thenThrowsException() {
        // given
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryFocusSessionPort.findByIdAndUserId(TEST_USER_ID, TEST_SESSION_ID))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> getFocusSessionDetailService.execute(TEST_SESSION_ID))
                .isInstanceOf(FocusSessionNotFoundException.class);
    }

    @Test
    @DisplayName("다른 사용자의 세션 조회 시 예외가 발생한다")
    void whenAccessingOtherUserSession_thenThrowsException() {
        // given
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryFocusSessionPort.findByIdAndUserId(TEST_USER_ID, TEST_SESSION_ID))
                .willReturn(Optional.empty()); // 다른 사용자의 세션이므로 조회 안됨

        // when & then
        assertThatThrownBy(() -> getFocusSessionDetailService.execute(TEST_SESSION_ID))
                .isInstanceOf(FocusSessionNotFoundException.class);
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
                null,
                null,
                LocalDateTime.now()
        );
    }

    /**
     * 테스트용 FocusSession 객체를 생성합니다.
     *
     * @return 생성된 FocusSession 객체
     */
    private FocusSession createFocusSession() {
        LocalDateTime startedAt = LocalDateTime.of(2024, 1, 15, 9, 0);
        LocalDateTime endedAt = LocalDateTime.of(2024, 1, 15, 11, 0);
        return new FocusSession(
                TEST_SESSION_ID,
                TEST_USER_ID,
                startedAt,
                endedAt,
                2 * 60 * 60,
                FocusTag.WORK,
                LocalDateTime.now()
        );
    }
}
