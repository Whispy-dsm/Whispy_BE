package whispy_server.whispy.domain.history.application.service.unit;

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
import whispy_server.whispy.domain.history.adapter.in.web.dto.response.ListeningHistoryResponse;
import whispy_server.whispy.domain.history.adapter.out.dto.ListeningHistoryWithMusicDto;
import whispy_server.whispy.domain.history.application.port.out.QueryListeningHistoryPort;
import whispy_server.whispy.domain.history.application.service.QueryListeningHistoryService;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * QueryListeningHistoryService의 단위 테스트 클래스
 *
 * 청취 이력 조회 서비스의 다양한 시나리오를 검증합니다.
 * 페이지네이션된 청취 이력 목록 조회 로직을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("QueryListeningHistoryService 테스트")
class QueryListeningHistoryServiceTest {

    @InjectMocks
    private QueryListeningHistoryService queryListeningHistoryService;

    @Mock
    private QueryListeningHistoryPort queryListeningHistoryPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_EMAIL = "test@example.com";

    @Test
    @DisplayName("현재 사용자의 청취 이력을 페이지네이션하여 조회할 수 있다")
    void whenQueryingHistory_thenReturnsPagedResults() {
        // given
        User user = createUser();
        Pageable pageable = PageRequest.of(0, 10);

        List<ListeningHistoryWithMusicDto> historyList = List.of(
                createHistoryDto(1L, 1L, "음악 1"),
                createHistoryDto(2L, 2L, "음악 2")
        );
        Page<ListeningHistoryWithMusicDto> historyPage = new PageImpl<>(historyList, pageable, historyList.size());

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryListeningHistoryPort.findListeningHistoryWithMusicByUserId(TEST_USER_ID, pageable))
                .willReturn(historyPage);

        // when
        Page<ListeningHistoryResponse> result = queryListeningHistoryService.execute(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        verify(userFacadeUseCase).currentUser();
        verify(queryListeningHistoryPort).findListeningHistoryWithMusicByUserId(TEST_USER_ID, pageable);
    }

    @Test
    @DisplayName("청취 이력이 없을 때 빈 페이지를 반환한다")
    void whenNoHistory_thenReturnsEmptyPage() {
        // given
        User user = createUser();
        Pageable pageable = PageRequest.of(0, 10);
        Page<ListeningHistoryWithMusicDto> emptyPage = Page.empty(pageable);

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryListeningHistoryPort.findListeningHistoryWithMusicByUserId(TEST_USER_ID, pageable))
                .willReturn(emptyPage);

        // when
        Page<ListeningHistoryResponse> result = queryListeningHistoryService.execute(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
    }

    @Test
    @DisplayName("두 번째 페이지를 조회할 수 있다")
    void whenQueryingSecondPage_thenReturnsCorrectPage() {
        // given
        User user = createUser();
        Pageable pageable = PageRequest.of(1, 5);

        List<ListeningHistoryWithMusicDto> historyList = List.of(
                createHistoryDto(6L, 6L, "음악 6"),
                createHistoryDto(7L, 7L, "음악 7")
        );
        Page<ListeningHistoryWithMusicDto> historyPage = new PageImpl<>(historyList, pageable, 12);

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryListeningHistoryPort.findListeningHistoryWithMusicByUserId(TEST_USER_ID, pageable))
                .willReturn(historyPage);

        // when
        Page<ListeningHistoryResponse> result = queryListeningHistoryService.execute(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(12);
        assertThat(result.getNumber()).isEqualTo(1);
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
     * 테스트용 ListeningHistoryWithMusicDto 객체를 생성합니다.
     *
     * @param historyId 이력 ID
     * @param musicId 음악 ID
     * @param musicTitle 음악 제목
     * @return 생성된 ListeningHistoryWithMusicDto 객체
     */
    private ListeningHistoryWithMusicDto createHistoryDto(Long historyId, Long musicId, String musicTitle) {
        return new ListeningHistoryWithMusicDto(
                musicId,
                musicTitle,
                "http://example.com/music.mp3",
                180,
                whispy_server.whispy.domain.music.model.type.MusicCategory.NATURE,
                LocalDateTime.now()
        );
    }
}
