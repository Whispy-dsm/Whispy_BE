package whispy_server.whispy.domain.music.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.music.adapter.in.web.dto.request.UpdateMusicRequest;
import whispy_server.whispy.domain.music.application.port.out.MusicSavePort;
import whispy_server.whispy.domain.music.application.port.out.QueryMusicPort;
import whispy_server.whispy.domain.music.application.service.UpdateMusicService;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.music.model.type.MusicCategory;
import whispy_server.whispy.global.exception.domain.music.MusicNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * UpdateMusicService의 단위 테스트 클래스
 *
 * 음악 수정 서비스의 다양한 시나리오를 검증합니다.
 * 음악 정보 수정 및 예외 처리 로직을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateMusicService 테스트")
class UpdateMusicServiceTest {

    @InjectMocks
    private UpdateMusicService updateMusicService;

    @Mock
    private QueryMusicPort queryMusicPort;

    @Mock
    private MusicSavePort musicSavePort;

    @Test
    @DisplayName("음악 정보를 수정할 수 있다")
    void whenValidRequest_thenUpdatesMusic() {
        // given
        Long musicId = 1L;
        Music existingMusic = new Music(
                musicId,
                "기존 제목",
                "Old Artist",
                "기존 설명",
                "http://example.com/old.mp3",
                180,
                MusicCategory.NATURE,
                "http://example.com/old-cover.jpg",
                "http://example.com/old-video.mp4"
        );

        UpdateMusicRequest request = new UpdateMusicRequest(
                musicId,
                "새로운 제목",
                "New Artist",
                "새로운 설명",
                "http://example.com/new.mp3",
                200,
                MusicCategory.NATURE,
                "http://example.com/new-cover.jpg",
                "http://example.com/new-video.mp4"
        );

        given(queryMusicPort.findById(musicId)).willReturn(Optional.of(existingMusic));

        // when
        updateMusicService.execute(request);

        // then
        verify(queryMusicPort).findById(musicId);
        verify(musicSavePort).save(any(Music.class));
    }

    @Test
    @DisplayName("존재하지 않는 음악 수정 시 예외가 발생한다")
    void whenMusicNotFound_thenThrowsException() {
        // given
        Long musicId = 999L;
        UpdateMusicRequest request = new UpdateMusicRequest(
                musicId,
                "새로운 제목",
                "New Artist",
                "새로운 설명",
                "http://example.com/new.mp3",
                200,
                MusicCategory.NATURE,
                "http://example.com/new-cover.jpg",
                "http://example.com/new-video.mp4"
        );

        given(queryMusicPort.findById(musicId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> updateMusicService.execute(request))
                .isInstanceOf(MusicNotFoundException.class);
        verify(queryMusicPort).findById(musicId);
    }
}
