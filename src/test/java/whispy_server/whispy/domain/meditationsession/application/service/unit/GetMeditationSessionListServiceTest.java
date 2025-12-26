package whispy_server.whispy.domain.meditationsession.application.service.unit;
import whispy_server.whispy.domain.meditationsession.application.service.GetMeditationSessionListService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response.MeditationSessionListResponse;
import whispy_server.whispy.domain.meditationsession.application.port.out.QueryMeditationSessionPort;
import whispy_server.whispy.domain.meditationsession.model.MeditationSession;
import whispy_server.whispy.domain.meditationsession.model.types.BreatheMode;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * GetMeditationSessionListService의 단위 테스트 클래스
 *
 * 명상 세션 목록 조회 서비스의 다양한 시나리오를 검증합니다.
 * 페이지네이션 기반 목록 조회를 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetMeditationSessionListService 테스트")
class GetMeditationSessionListServiceTest {

    @InjectMocks
    private GetMeditationSessionListService getMeditationSessionListService;

    @Mock
    private QueryMeditationSessionPort queryMeditationSessionPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;

    @Test
    @DisplayName("명상 세션 목록을 페이지네이션하여 조회할 수 있다")
    void whenRequestingList_thenReturnsPagedSessions() {
        // given
        User user = createUser();
        Pageable pageable = PageRequest.of(0, 10);

        List<MeditationSession> sessions = List.of(
                createMeditationSession(1L, BreatheMode.BOX_BREATHING),
                createMeditationSession(2L, BreatheMode.DIAPHRAGMATIC),
                createMeditationSession(3L, BreatheMode.ALTERNATE_NOSTRIL)
        );
        Page<MeditationSession> sessionPage = new PageImpl<>(sessions, pageable, sessions.size());

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryMeditationSessionPort.findByUserId(eq(TEST_USER_ID), any(Pageable.class)))
                .willReturn(sessionPage);

        // when
        Page<MeditationSessionListResponse> response = getMeditationSessionListService.execute(pageable);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(3);
        assertThat(response.getTotalElements()).isEqualTo(3);
    }

    @Test
    @DisplayName("세션이 없는 경우 빈 페이지를 반환한다")
    void whenNoSessions_thenReturnsEmptyPage() {
        // given
        User user = createUser();
        Pageable pageable = PageRequest.of(0, 10);
        Page<MeditationSession> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryMeditationSessionPort.findByUserId(eq(TEST_USER_ID), any(Pageable.class)))
                .willReturn(emptyPage);

        // when
        Page<MeditationSessionListResponse> response = getMeditationSessionListService.execute(pageable);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEmpty();
        assertThat(response.getTotalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("현재 사용자의 세션만 조회한다")
    void whenRequestingList_thenRetrievesCurrentUserSessionsOnly() {
        // given
        User user = createUser();
        Pageable pageable = PageRequest.of(0, 10);
        Page<MeditationSession> sessionPage = new PageImpl<>(List.of(), pageable, 0);

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryMeditationSessionPort.findByUserId(eq(TEST_USER_ID), any(Pageable.class)))
                .willReturn(sessionPage);

        // when
        getMeditationSessionListService.execute(pageable);

        // then
        verify(queryMeditationSessionPort).findByUserId(eq(TEST_USER_ID), any(Pageable.class));
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
     * @param id 세션 ID
     * @param breatheMode 호흡 모드
     * @return 생성된 MeditationSession 객체
     */
    private MeditationSession createMeditationSession(Long id, BreatheMode breatheMode) {
        LocalDateTime startedAt = LocalDateTime.of(2024, 1, 15, 7, 0);
        LocalDateTime endedAt = LocalDateTime.of(2024, 1, 15, 7, 30);
        return new MeditationSession(
                id,
                TEST_USER_ID,
                startedAt,
                endedAt,
                30 * 60,
                breatheMode,
                LocalDateTime.now()
        );
    }
}
