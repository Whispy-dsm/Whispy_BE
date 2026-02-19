package whispy_server.whispy.domain.sleepsession.application.service.unit;
import whispy_server.whispy.domain.sleepsession.application.service.SaveSleepSessionService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.request.SaveSleepSessionRequest;
import whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response.SleepSessionResponse;
import whispy_server.whispy.domain.sleepsession.application.port.out.SleepSessionSavePort;
import whispy_server.whispy.domain.sleepsession.model.SleepSession;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.global.cache.version.StatisticsCacheVersionManager;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * SaveSleepSessionService의 단위 테스트 클래스
 *
 * 수면 세션 저장 서비스의 다양한 시나리오를 검증합니다.
 * 수면 세션 생성 및 저장 로직을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SaveSleepSessionService 테스트")
class SaveSleepSessionServiceTest {

    @InjectMocks
    private SaveSleepSessionService saveSleepSessionService;

    @Mock
    private SleepSessionSavePort sleepSessionSavePort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    @Mock
    private StatisticsCacheVersionManager statisticsCacheVersionManager;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;

    @Test
    @DisplayName("유효한 수면 세션을 저장할 수 있다")
    void whenValidSleepSession_thenSavesSuccessfully() {
        // given
        User user = createUser();
        LocalDateTime startedAt = LocalDateTime.of(2024, 1, 15, 23, 0);
        LocalDateTime endedAt = LocalDateTime.of(2024, 1, 16, 7, 0);
        int durationSeconds = 8 * 60 * 60; // 8시간

        SaveSleepSessionRequest request = new SaveSleepSessionRequest(
                startedAt,
                endedAt,
                durationSeconds
        );

        SleepSession savedSession = new SleepSession(
                1L,
                TEST_USER_ID,
                startedAt,
                endedAt,
                durationSeconds,
                LocalDateTime.now()
        );

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(sleepSessionSavePort.save(any(SleepSession.class))).willReturn(savedSession);

        // when
        SleepSessionResponse response = saveSleepSessionService.execute(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.userId()).isEqualTo(TEST_USER_ID);
        assertThat(response.startedAt()).isEqualTo(startedAt);
        assertThat(response.endedAt()).isEqualTo(endedAt);
        assertThat(response.durationSeconds()).isEqualTo(durationSeconds);
        verify(sleepSessionSavePort).save(any(SleepSession.class));
    }

    @Test
    @DisplayName("현재 사용자 정보로 세션을 생성한다")
    void whenSavingSession_thenUsesCurrentUserInfo() {
        // given
        User user = createUser();
        LocalDateTime startedAt = LocalDateTime.of(2024, 1, 15, 23, 0);
        LocalDateTime endedAt = LocalDateTime.of(2024, 1, 16, 7, 0);
        int durationSeconds = 8 * 60 * 60;

        SaveSleepSessionRequest request = new SaveSleepSessionRequest(
                startedAt,
                endedAt,
                durationSeconds
        );

        SleepSession savedSession = new SleepSession(
                1L,
                TEST_USER_ID,
                startedAt,
                endedAt,
                durationSeconds,
                LocalDateTime.now()
        );

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(sleepSessionSavePort.save(any(SleepSession.class))).willReturn(savedSession);

        // when
        saveSleepSessionService.execute(request);

        // then
        verify(userFacadeUseCase).currentUser();
    }

    @Test
    @DisplayName("짧은 수면 세션도 저장할 수 있다")
    void whenShortSleepSession_thenSavesSuccessfully() {
        // given
        User user = createUser();
        LocalDateTime startedAt = LocalDateTime.of(2024, 1, 15, 14, 0);
        LocalDateTime endedAt = LocalDateTime.of(2024, 1, 15, 14, 30);
        int durationSeconds = 30 * 60; // 30분

        SaveSleepSessionRequest request = new SaveSleepSessionRequest(
                startedAt,
                endedAt,
                durationSeconds
        );

        SleepSession savedSession = new SleepSession(
                1L,
                TEST_USER_ID,
                startedAt,
                endedAt,
                durationSeconds,
                LocalDateTime.now()
        );

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(sleepSessionSavePort.save(any(SleepSession.class))).willReturn(savedSession);

        // when
        SleepSessionResponse response = saveSleepSessionService.execute(request);

        // then
        assertThat(response.durationSeconds()).isEqualTo(30 * 60);
    }

    @Test
    @DisplayName("긴 수면 세션도 저장할 수 있다")
    void whenLongSleepSession_thenSavesSuccessfully() {
        // given
        User user = createUser();
        LocalDateTime startedAt = LocalDateTime.of(2024, 1, 15, 22, 0);
        LocalDateTime endedAt = LocalDateTime.of(2024, 1, 16, 10, 0);
        int durationSeconds = 12 * 60 * 60; // 12시간

        SaveSleepSessionRequest request = new SaveSleepSessionRequest(
                startedAt,
                endedAt,
                durationSeconds
        );

        SleepSession savedSession = new SleepSession(
                1L,
                TEST_USER_ID,
                startedAt,
                endedAt,
                durationSeconds,
                LocalDateTime.now()
        );

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(sleepSessionSavePort.save(any(SleepSession.class))).willReturn(savedSession);

        // when
        SleepSessionResponse response = saveSleepSessionService.execute(request);

        // then
        assertThat(response.durationSeconds()).isEqualTo(12 * 60 * 60);
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
