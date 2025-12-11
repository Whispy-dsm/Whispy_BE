package whispy_server.whispy.domain.music.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.history.application.port.out.DeleteListeningHistoryPort;
import whispy_server.whispy.domain.like.application.port.out.DeleteMusicLikePort;
import whispy_server.whispy.domain.music.application.port.out.MusicDeletePort;
import whispy_server.whispy.domain.music.application.port.out.QueryMusicPort;
import whispy_server.whispy.domain.music.application.service.DeleteMusicService;
import whispy_server.whispy.domain.soundspace.application.port.out.DeleteSoundSpaceMusicPort;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * DeleteMusicService의 단위 테스트 클래스
 * <p>
 * 음악 삭제 서비스의 다양한 시나리오를 검증합니다.
 * 음악 삭제 로직을 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteMusicService 테스트")
class DeleteMusicServiceTest {

    @InjectMocks
    private DeleteMusicService deleteMusicService;

    @Mock
    private QueryMusicPort queryMusicPort;

    @Mock
    private MusicDeletePort musicDeletePort;

    @Mock
    private DeleteMusicLikePort deleteMusicLikePort;

    @Mock
    private DeleteListeningHistoryPort deleteListeningHistoryPort;

    @Mock
    private DeleteSoundSpaceMusicPort deleteSoundSpaceMusicPort;

    @Test
    @DisplayName("음악을 삭제할 수 있다")
    void whenValidId_thenDeletesSuccessfully() {
        // given
        Long musicId = 1L;
        given(queryMusicPort.existsById(musicId)).willReturn(true);

        // when
        deleteMusicService.execute(musicId);

        // then
        verify(queryMusicPort).existsById(musicId);
        verify(deleteMusicLikePort).deleteAllByMusicId(musicId);
        verify(deleteListeningHistoryPort).deleteAllByMusicId(musicId);
        verify(deleteSoundSpaceMusicPort).deleteAllByMusicId(musicId);
        verify(musicDeletePort).deleteById(musicId);
    }
}
