package whispy_server.whispy.domain.music.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.music.adapter.in.web.dto.response.MusicDetailResponse;
import whispy_server.whispy.domain.music.application.port.out.QueryMusicPort;
import whispy_server.whispy.domain.music.application.service.GetMusicDetailService;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.music.model.type.MusicCategory;
import whispy_server.whispy.global.exception.domain.music.MusicNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * GetMusicDetailService의 단위 테스트 클래스
 *
 * 음악 상세 조회 서비스의 다양한 시나리오를 검증합니다.
 * 음악 상세 정보 조회 및 예외 처리 로직을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetMusicDetailService 테스트")
class GetMusicDetailServiceTest {

    @InjectMocks
    private GetMusicDetailService getMusicDetailService;

    @Mock
    private QueryMusicPort queryMusicPort;

    @Test
    @DisplayName("음악 상세 정보를 조회할 수 있다")
    void whenMusicExists_thenReturnsDetails() {
        // given
        Long musicId = 1L;
        Music music = new Music(
                musicId,
                "빗소리",
                "Nature Sounds",
                "편안한 빗소리",
                "http://example.com/music.mp3",
                180,
                MusicCategory.NATURE,
                "http://example.com/cover.jpg",
                "http://example.com/video.mp4"
        );

        given(queryMusicPort.findById(musicId)).willReturn(Optional.of(music));

        // when
        MusicDetailResponse response = getMusicDetailService.execute(musicId);

        // then
        assertThat(response).isNotNull();
        verify(queryMusicPort).findById(musicId);
    }

    @Test
    @DisplayName("존재하지 않는 음악 조회 시 예외가 발생한다")
    void whenMusicNotFound_thenThrowsException() {
        // given
        Long musicId = 999L;
        given(queryMusicPort.findById(musicId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> getMusicDetailService.execute(musicId))
                .isInstanceOf(MusicNotFoundException.class);
        verify(queryMusicPort).findById(musicId);
    }
}
