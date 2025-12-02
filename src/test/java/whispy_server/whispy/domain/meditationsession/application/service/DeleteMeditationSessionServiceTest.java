package whispy_server.whispy.domain.meditationsession.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.meditationsession.application.port.out.DeleteMeditationSessionPort;
import whispy_server.whispy.domain.meditationsession.application.port.out.QueryMeditationSessionPort;
import whispy_server.whispy.domain.meditationsession.model.MeditationSession;
import whispy_server.whispy.domain.meditationsession.model.types.BreatheMode;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.exception.domain.meditationsession.MeditationSessionNotFoundException;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * DeleteMeditationSessionService의 단위 테스트 클래스
 * <p>
 * 명상 세션 삭제 서비스의 다양한 시나리오를 검증합니다.
 * 세션 삭제 및 소유권 검증 로직을 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteMeditationSessionService 테스트")
class DeleteMeditationSessionServiceTest {

    @InjectMocks
    private DeleteMeditationSessionService deleteMeditationSessionService;

    @Mock
    private QueryMeditationSessionPort queryMeditationSessionPort;

    @Mock
    private DeleteMeditationSessionPort deleteMeditationSessionPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_SESSION_ID = 100L;

    @Test
    @DisplayName("존재하는 세션을 삭제할 수 있다")
    void whenSessionExists_thenDeletesSuccessfully() {
        // given
        User user = createUser();
        MeditationSession session = createMeditationSession();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryMeditationSessionPort.findByIdAndUserId(TEST_SESSION_ID, TEST_USER_ID))
                .willReturn(Optional.of(session));

        // when
        deleteMeditationSessionService.execute(TEST_SESSION_ID);

        // then
        verify(deleteMeditationSessionPort).deleteById(TEST_SESSION_ID);
    }

    @Test
    @DisplayName("존재하지 않는 세션 삭제 시 예외가 발생한다")
    void whenSessionNotExists_thenThrowsException() {
        // given
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryMeditationSessionPort.findByIdAndUserId(TEST_SESSION_ID, TEST_USER_ID))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> deleteMeditationSessionService.execute(TEST_SESSION_ID))
                .isInstanceOf(MeditationSessionNotFoundException.class);
        verify(deleteMeditationSessionPort, never()).deleteById(TEST_SESSION_ID);
    }

    @Test
    @DisplayName("다른 사용자의 세션 삭제 시 예외가 발생한다")
    void whenDeletingOtherUserSession_thenThrowsException() {
        // given
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryMeditationSessionPort.findByIdAndUserId(TEST_SESSION_ID, TEST_USER_ID))
                .willReturn(Optional.empty()); // 다른 사용자의 세션이므로 조회 안됨

        // when & then
        assertThatThrownBy(() -> deleteMeditationSessionService.execute(TEST_SESSION_ID))
                .isInstanceOf(MeditationSessionNotFoundException.class);
        verify(deleteMeditationSessionPort, never()).deleteById(TEST_SESSION_ID);
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
     * 테스트용 MeditationSession 객체를 생성합니다.
     *
     * @return 생성된 MeditationSession 객체
     */
    private MeditationSession createMeditationSession() {
        LocalDateTime startedAt = LocalDateTime.of(2024, 1, 15, 7, 0);
        LocalDateTime endedAt = LocalDateTime.of(2024, 1, 15, 7, 30);
        return new MeditationSession(
                TEST_SESSION_ID,
                TEST_USER_ID,
                startedAt,
                endedAt,
                30 * 60,
                BreatheMode.BOX_BREATHING,
                LocalDateTime.now()
        );
    }
}
