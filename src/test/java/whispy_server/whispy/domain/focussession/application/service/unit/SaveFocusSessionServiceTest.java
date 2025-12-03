package whispy_server.whispy.domain.focussession.application.service.unit;
import whispy_server.whispy.domain.focussession.application.service.SaveFocusSessionService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.request.SaveFocusSessionRequest;
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.response.FocusSessionResponse;
import whispy_server.whispy.domain.focussession.application.port.out.FocusSessionSavePort;
import whispy_server.whispy.domain.focussession.model.FocusSession;
import whispy_server.whispy.domain.focussession.model.types.FocusTag;
import whispy_server.whispy.domain.statistics.focus.daily.application.port.out.QueryFocusStatisticsPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * SaveFocusSessionService의 단위 테스트 클래스
 * <p>
 * 집중 세션 저장 서비스의 다양한 시나리오를 검증합니다.
 * 집중 세션 생성 및 저장, 오늘의 총 집중 시간 계산을 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SaveFocusSessionService 테스트")
class SaveFocusSessionServiceTest {

    @InjectMocks
    private SaveFocusSessionService saveFocusSessionService;

    @Mock
    private FocusSessionSavePort focusSessionSavePort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    @Mock
    private QueryFocusStatisticsPort queryFocusStatisticsPort;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;

    @Test
    @DisplayName("유효한 집중 세션을 저장할 수 있다")
    void whenValidFocusSession_thenSavesSuccessfully() {
        // given
        User user = createUser();
        LocalDateTime startedAt = LocalDateTime.of(2024, 1, 15, 9, 0);
        LocalDateTime endedAt = LocalDateTime.of(2024, 1, 15, 11, 0);
        int durationSeconds = 2 * 60 * 60; // 2시간

        SaveFocusSessionRequest request = new SaveFocusSessionRequest(
                startedAt,
                endedAt,
                durationSeconds,
                FocusTag.WORK
        );

        FocusSession savedSession = new FocusSession(
                1L,
                TEST_USER_ID,
                startedAt,
                endedAt,
                durationSeconds,
                FocusTag.WORK,
                LocalDateTime.now()
        );

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(focusSessionSavePort.save(any(FocusSession.class))).willReturn(savedSession);
        given(queryFocusStatisticsPort.getTotalMinutes(anyLong(), any(), any())).willReturn(120);

        // when
        FocusSessionResponse response = saveFocusSessionService.execute(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.userId()).isEqualTo(TEST_USER_ID);
        assertThat(response.tag()).isEqualTo(FocusTag.WORK);
        assertThat(response.todayTotalMinutes()).isEqualTo(120);
        verify(focusSessionSavePort).save(any(FocusSession.class));
    }

    @Test
    @DisplayName("오늘의 총 집중 시간을 계산한다")
    void whenSavingSession_thenCalculatesTodayTotal() {
        // given
        User user = createUser();
        LocalDateTime startedAt = LocalDateTime.of(2024, 1, 15, 14, 0);
        LocalDateTime endedAt = LocalDateTime.of(2024, 1, 15, 15, 0);
        int durationSeconds = 60 * 60; // 1시간

        SaveFocusSessionRequest request = new SaveFocusSessionRequest(
                startedAt,
                endedAt,
                durationSeconds,
                FocusTag.STUDY
        );

        FocusSession savedSession = new FocusSession(
                1L,
                TEST_USER_ID,
                startedAt,
                endedAt,
                durationSeconds,
                FocusTag.STUDY,
                LocalDateTime.now()
        );

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(focusSessionSavePort.save(any(FocusSession.class))).willReturn(savedSession);
        given(queryFocusStatisticsPort.getTotalMinutes(anyLong(), any(), any())).willReturn(180);

        // when
        FocusSessionResponse response = saveFocusSessionService.execute(request);

        // then
        assertThat(response.todayTotalMinutes()).isEqualTo(180);
        verify(queryFocusStatisticsPort).getTotalMinutes(anyLong(), any(), any());
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
}
