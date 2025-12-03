package whispy_server.whispy.domain.meditationsession.application.service.unit;
import whispy_server.whispy.domain.meditationsession.application.service.SaveMeditationSessionService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.request.SaveMeditationSessionRequest;
import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response.MeditationSessionResponse;
import whispy_server.whispy.domain.meditationsession.application.port.out.MeditationSessionSavePort;
import whispy_server.whispy.domain.meditationsession.model.MeditationSession;
import whispy_server.whispy.domain.statistics.meditation.daily.application.port.out.QueryMeditationStatisticsPort;
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
 * SaveMeditationSessionService의 단위 테스트 클래스
 * <p>
 * 명상 세션 저장 서비스의 다양한 시나리오를 검증합니다.
 * 명상 세션 생성 및 저장, 오늘의 총 명상 시간 계산을 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SaveMeditationSessionService 테스트")
class SaveMeditationSessionServiceTest {

    @InjectMocks
    private SaveMeditationSessionService saveMeditationSessionService;

    @Mock
    private MeditationSessionSavePort meditationSessionSavePort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    @Mock
    private QueryMeditationStatisticsPort queryMeditationStatisticsPort;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;

    @Test
    @DisplayName("유효한 명상 세션을 저장할 수 있다")
    void whenValidMeditationSession_thenSavesSuccessfully() {
        // given
        User user = createUser();
        LocalDateTime startedAt = LocalDateTime.of(2024, 1, 15, 7, 0);
        LocalDateTime endedAt = LocalDateTime.of(2024, 1, 15, 7, 30);
        int durationSeconds = 30 * 60; // 30분

        SaveMeditationSessionRequest request = new SaveMeditationSessionRequest(
                startedAt,
                endedAt,
                durationSeconds,
                whispy_server.whispy.domain.meditationsession.model.types.BreatheMode.BOX_BREATHING
        );

        MeditationSession savedSession = new MeditationSession(
                1L,
                TEST_USER_ID,
                startedAt,
                endedAt,
                durationSeconds,
                whispy_server.whispy.domain.meditationsession.model.types.BreatheMode.BOX_BREATHING,
                LocalDateTime.now()
        );

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(meditationSessionSavePort.save(any(MeditationSession.class))).willReturn(savedSession);
        given(queryMeditationStatisticsPort.getTotalMinutes(anyLong(), any(), any())).willReturn(30);

        // when
        MeditationSessionResponse response = saveMeditationSessionService.execute(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.userId()).isEqualTo(TEST_USER_ID);
        assertThat(response.breatheMode()).isEqualTo(whispy_server.whispy.domain.meditationsession.model.types.BreatheMode.BOX_BREATHING);
        assertThat(response.todayTotalMinutes()).isEqualTo(30);
        verify(meditationSessionSavePort).save(any(MeditationSession.class));
    }

    @Test
    @DisplayName("오늘의 총 명상 시간을 계산한다")
    void whenSavingSession_thenCalculatesTodayTotal() {
        // given
        User user = createUser();
        LocalDateTime startedAt = LocalDateTime.of(2024, 1, 15, 19, 0);
        LocalDateTime endedAt = LocalDateTime.of(2024, 1, 15, 19, 15);
        int durationSeconds = 15 * 60; // 15분

        SaveMeditationSessionRequest request = new SaveMeditationSessionRequest(
                startedAt,
                endedAt,
                durationSeconds,
                whispy_server.whispy.domain.meditationsession.model.types.BreatheMode.DIAPHRAGMATIC
        );

        MeditationSession savedSession = new MeditationSession(
                1L,
                TEST_USER_ID,
                startedAt,
                endedAt,
                durationSeconds,
                whispy_server.whispy.domain.meditationsession.model.types.BreatheMode.DIAPHRAGMATIC,
                LocalDateTime.now()
        );

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(meditationSessionSavePort.save(any(MeditationSession.class))).willReturn(savedSession);
        given(queryMeditationStatisticsPort.getTotalMinutes(anyLong(), any(), any())).willReturn(45);

        // when
        MeditationSessionResponse response = saveMeditationSessionService.execute(request);

        // then
        assertThat(response.todayTotalMinutes()).isEqualTo(45);
        verify(queryMeditationStatisticsPort).getTotalMinutes(anyLong(), any(), any());
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
