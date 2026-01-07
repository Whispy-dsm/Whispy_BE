package whispy_server.whispy.domain.music.application.service.unit;

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
import whispy_server.whispy.domain.music.adapter.in.web.dto.response.MusicSearchResponse;
import whispy_server.whispy.domain.music.application.port.out.SearchMusicPort;
import whispy_server.whispy.domain.music.application.service.SearchMusicTitleService;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.music.model.type.MusicCategory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * SearchMusicTitleService의 단위 테스트 클래스
 *
 * 제목 기반 음악 검색 서비스의 다양한 시나리오를 검증합니다.
 * 제목으로 음악 검색 로직을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SearchMusicTitleService 테스트")
class SearchMusicTitleServiceTest {

    @InjectMocks
    private SearchMusicTitleService searchMusicTitleService;

    @Mock
    private SearchMusicPort searchMusicPort;

    @Test
    @DisplayName("제목으로 음악을 검색할 수 있다")
    void whenSearchingByTitle_thenReturnsMusics() {
        // given
        String keyword = "빗소리";
        Pageable pageable = PageRequest.of(0, 10);

        List<Music> musics = List.of(
                new Music(1L, "빗소리 1", "Nature Sounds", "편안한 빗소리", "http://example.com/music1.mp3", 180, MusicCategory.NATURE, "http://example.com/cover1.jpg", "http://example.com/video1.mp4"),
                new Music(2L, "빗소리 2", "Nature Sounds", "부드러운 빗소리", "http://example.com/music2.mp3", 200, MusicCategory.NATURE, "http://example.com/cover2.jpg", "http://example.com/video2.mp4")
        );
        Page<Music> musicPage = new PageImpl<>(musics, pageable, musics.size());

        given(searchMusicPort.searchByTitle(keyword, pageable)).willReturn(musicPage);

        // when
        Page<MusicSearchResponse> result = searchMusicTitleService.searchMusic(keyword, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        verify(searchMusicPort).searchByTitle(keyword, pageable);
    }

    @Test
    @DisplayName("검색 결과가 없을 때 빈 페이지를 반환한다")
    void whenNoMatchingMusics_thenReturnsEmptyPage() {
        // given
        String keyword = "존재하지않는음악";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Music> emptyPage = Page.empty(pageable);

        given(searchMusicPort.searchByTitle(keyword, pageable)).willReturn(emptyPage);

        // when
        Page<MusicSearchResponse> result = searchMusicTitleService.searchMusic(keyword, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
    }
}
