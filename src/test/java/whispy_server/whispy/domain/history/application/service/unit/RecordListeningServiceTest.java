package whispy_server.whispy.domain.history.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.history.application.port.out.QueryListeningHistoryPort;
import whispy_server.whispy.domain.history.application.port.out.SaveListeningHistoryPort;
import whispy_server.whispy.domain.history.application.service.RecordListeningService;
import whispy_server.whispy.domain.history.model.ListeningHistory;
import whispy_server.whispy.domain.music.application.port.out.QueryMusicPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.exception.domain.music.MusicNotFoundException;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * RecordListeningService의 단위 테스트 클래스
 *
 * 청취 기록 저장 서비스의 다양한 시나리오를 검증합니다.
 * 새로운 청취 기록 생성 및 기존 기록 갱신 로직을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RecordListeningService 테스트")
class RecordListeningServiceTest {

    @InjectMocks
    private RecordListeningService recordListeningService;

    @Mock
    private SaveListeningHistoryPort saveListeningHistoryPort;

    @Mock
    private QueryListeningHistoryPort queryListeningHistoryPort;

    @Mock
    private QueryMusicPort queryMusicPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_MUSIC_ID = 10L;
    private static final String TEST_EMAIL = "test@example.com";

    @Test
    @DisplayName("새로운 음악 청취 기록을 저장할 수 있다")
    void whenFirstListening_thenCreatesNewHistory() {
        // given
        User user = createUser();

        given(queryMusicPort.existsById(TEST_MUSIC_ID)).willReturn(true);
        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryListeningHistoryPort.findByUserIdAndMusicId(TEST_USER_ID, TEST_MUSIC_ID))
                .willReturn(Optional.empty());

        // when
        recordListeningService.execute(TEST_MUSIC_ID);

        // then
        verify(queryMusicPort).existsById(TEST_MUSIC_ID);
        verify(userFacadeUseCase).currentUser();
        verify(queryListeningHistoryPort).findByUserIdAndMusicId(TEST_USER_ID, TEST_MUSIC_ID);
        verify(saveListeningHistoryPort).save(any(ListeningHistory.class));
    }

    @Test
    @DisplayName("기존 청취 기록이 있으면 갱신한다")
    void whenExistingHistory_thenUpdatesListenedAt() {
        // given
        User user = createUser();
        ListeningHistory existingHistory = new ListeningHistory(
                1L,
                TEST_USER_ID,
                TEST_MUSIC_ID,
                LocalDateTime.now().minusDays(1)
        );

        given(queryMusicPort.existsById(TEST_MUSIC_ID)).willReturn(true);
        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryListeningHistoryPort.findByUserIdAndMusicId(TEST_USER_ID, TEST_MUSIC_ID))
                .willReturn(Optional.of(existingHistory));

        // when
        recordListeningService.execute(TEST_MUSIC_ID);

        // then
        verify(queryMusicPort).existsById(TEST_MUSIC_ID);
        verify(saveListeningHistoryPort).save(any(ListeningHistory.class));
    }

    @Test
    @DisplayName("존재하지 않는 음악에 대해 예외가 발생한다")
    void whenMusicNotFound_thenThrowsException() {
        // given
        given(queryMusicPort.existsById(TEST_MUSIC_ID)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> recordListeningService.execute(TEST_MUSIC_ID))
                .isInstanceOf(MusicNotFoundException.class);
        verify(queryMusicPort).existsById(TEST_MUSIC_ID);
    }

    @Test
    @DisplayName("다른 음악의 청취 기록을 저장할 수 있다")
    void whenDifferentMusic_thenSavesSuccessfully() {
        // given
        Long differentMusicId = 20L;
        User user = createUser();

        given(queryMusicPort.existsById(differentMusicId)).willReturn(true);
        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryListeningHistoryPort.findByUserIdAndMusicId(TEST_USER_ID, differentMusicId))
                .willReturn(Optional.empty());

        // when
        recordListeningService.execute(differentMusicId);

        // then
        verify(saveListeningHistoryPort).save(any(ListeningHistory.class));
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
