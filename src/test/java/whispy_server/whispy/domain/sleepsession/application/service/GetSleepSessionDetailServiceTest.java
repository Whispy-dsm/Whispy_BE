package whispy_server.whispy.domain.sleepsession.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response.SleepSessionDetailResponse;
import whispy_server.whispy.domain.sleepsession.application.port.out.QuerySleepSessionPort;
import whispy_server.whispy.domain.sleepsession.model.SleepSession;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.exception.domain.sleepsession.SleepSessionNotFoundException;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

/**
 * GetSleepSessionDetailService의 단위 테스트 클래스
 * <p>
 * 수면 세션 상세 조회 서비스의 다양한 시나리오를 검증합니다.
 * 세션 조회 및 소유권 검증 로직을 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetSleepSessionDetailService 테스트")
class GetSleepSessionDetailServiceTest {

    @InjectMocks
    private GetSleepSessionDetailService getSleepSessionDetailService;

    @Mock
    private QuerySleepSessionPort querySleepSessionPort;

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
        SleepSession session = createSleepSession();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(querySleepSessionPort.findByIdAndUserId(TEST_SESSION_ID, TEST_USER_ID))
                .willReturn(Optional.of(session));

        // when
        SleepSessionDetailResponse response = getSleepSessionDetailService.execute(TEST_SESSION_ID);

        // then
        assertThat(response).isNotNull();
        assertThat(response.durationMinutes()).isEqualTo(8 * 60);
        assertThat(response.startedAt()).isNotNull();
        assertThat(response.endedAt()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 세션 조회 시 예외가 발생한다")
    void whenSessionNotExists_thenThrowsException() {
        // given
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(querySleepSessionPort.findByIdAndUserId(TEST_SESSION_ID, TEST_USER_ID))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> getSleepSessionDetailService.execute(TEST_SESSION_ID))
                .isInstanceOf(SleepSessionNotFoundException.class);
    }

    @Test
    @DisplayName("다른 사용자의 세션 조회 시 예외가 발생한다")
    void whenAccessingOtherUserSession_thenThrowsException() {
        // given
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(querySleepSessionPort.findByIdAndUserId(TEST_SESSION_ID, TEST_USER_ID))
                .willReturn(Optional.empty()); // 다른 사용자의 세션이므로 조회 안됨

        // when & then
        assertThatThrownBy(() -> getSleepSessionDetailService.execute(TEST_SESSION_ID))
                .isInstanceOf(SleepSessionNotFoundException.class);
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
