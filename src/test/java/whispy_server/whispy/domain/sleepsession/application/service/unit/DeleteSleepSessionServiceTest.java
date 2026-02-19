package whispy_server.whispy.domain.sleepsession.application.service.unit;
import whispy_server.whispy.domain.sleepsession.application.service.DeleteSleepSessionService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.sleepsession.application.port.out.DeleteSleepSessionPort;
import whispy_server.whispy.domain.sleepsession.application.port.out.QuerySleepSessionPort;
import whispy_server.whispy.domain.sleepsession.model.SleepSession;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.exception.domain.sleepsession.SleepSessionNotFoundException;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.global.utils.redis.StatisticsCacheVersionManager;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * DeleteSleepSessionService의 단위 테스트 클래스
 *
 * 수면 세션 삭제 서비스의 다양한 시나리오를 검증합니다.
 * 세션 삭제 및 소유권 검증 로직을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteSleepSessionService 테스트")
class DeleteSleepSessionServiceTest {

    @InjectMocks
    private DeleteSleepSessionService deleteSleepSessionService;

    @Mock
    private QuerySleepSessionPort querySleepSessionPort;

    @Mock
    private DeleteSleepSessionPort deleteSleepSessionPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    @Mock
    private StatisticsCacheVersionManager statisticsCacheVersionManager;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_SESSION_ID = 100L;

    @Test
    @DisplayName("존재하는 세션을 삭제할 수 있다")
    void whenSessionExists_thenDeletesSuccessfully() {
        // given
        User user = createUser();
        SleepSession session = createSleepSession();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(querySleepSessionPort.findByIdAndUserId(TEST_SESSION_ID, TEST_USER_ID))
                .willReturn(Optional.of(session));

        // when
        deleteSleepSessionService.execute(TEST_SESSION_ID);

        // then
        verify(deleteSleepSessionPort).deleteById(TEST_SESSION_ID);
    }

    @Test
    @DisplayName("존재하지 않는 세션 삭제 시 예외가 발생한다")
    void whenSessionNotExists_thenThrowsException() {
        // given
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(querySleepSessionPort.findByIdAndUserId(TEST_SESSION_ID, TEST_USER_ID))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> deleteSleepSessionService.execute(TEST_SESSION_ID))
                .isInstanceOf(SleepSessionNotFoundException.class);
        verify(deleteSleepSessionPort, never()).deleteById(TEST_SESSION_ID);
    }

    @Test
    @DisplayName("다른 사용자의 세션 삭제 시 예외가 발생한다")
    void whenDeletingOtherUserSession_thenThrowsException() {
        // given
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(querySleepSessionPort.findByIdAndUserId(TEST_SESSION_ID, TEST_USER_ID))
                .willReturn(Optional.empty()); // 다른 사용자의 세션이므로 조회 안됨

        // when & then
        assertThatThrownBy(() -> deleteSleepSessionService.execute(TEST_SESSION_ID))
                .isInstanceOf(SleepSessionNotFoundException.class);
        verify(deleteSleepSessionPort, never()).deleteById(TEST_SESSION_ID);
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
     * 테스트용 SleepSession 객체를 생성합니다.
     *
     * @return 생성된 SleepSession 객체
     */
    private SleepSession createSleepSession() {
        LocalDateTime startedAt = LocalDateTime.of(2024, 1, 15, 23, 0);
        LocalDateTime endedAt = LocalDateTime.of(2024, 1, 16, 7, 0);
        return new SleepSession(
                TEST_SESSION_ID,
                TEST_USER_ID,
                startedAt,
                endedAt,
                8 * 60 * 60,
                LocalDateTime.now()
        );
    }
}
