package whispy_server.whispy.domain.music.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.music.adapter.in.web.dto.request.CreateMusicRequest;
import whispy_server.whispy.domain.music.application.port.out.MusicSavePort;
import whispy_server.whispy.domain.music.application.service.CreateMusicService;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.music.model.type.MusicCategory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * CreateMusicService의 단위 테스트 클래스
 *
 * 음악 생성 서비스의 다양한 시나리오를 검증합니다.
 * 음악 생성 및 저장 로직을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CreateMusicService 테스트")
class CreateMusicServiceTest {

    @InjectMocks
    private CreateMusicService createMusicService;

    @Mock
    private MusicSavePort musicSavePort;

    @Test
    @DisplayName("음악을 정상적으로 생성할 수 있다")
    void whenValidMusic_thenCreatesSuccessfully() {
        // given
        CreateMusicRequest request = new CreateMusicRequest(
                "빗소리",
                "http://example.com/music.mp3",
                180,
                MusicCategory.NATURE,
                "http://example.com/cover.jpg",
                "http://example.com/video.mp4"
        );

        // when
        createMusicService.execute(request);

        // then
        verify(musicSavePort).save(any(Music.class));
    }

    @Test
    @DisplayName("배너 이미지 없이 음악을 생성할 수 있다")
    void whenNoBannerImage_thenCreatesSuccessfully() {
        // given
        CreateMusicRequest request = new CreateMusicRequest(
                "자연의 소리",
                "http://example.com/nature.mp3",
                240,
                MusicCategory.NATURE,
                null,
                null
        );

        // when
        createMusicService.execute(request);

        // then
        verify(musicSavePort).save(any(Music.class));
    }
}
