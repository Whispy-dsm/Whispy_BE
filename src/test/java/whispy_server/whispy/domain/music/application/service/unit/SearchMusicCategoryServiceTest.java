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
import whispy_server.whispy.domain.music.application.service.MusicCategorySearchCacheValue;
import whispy_server.whispy.domain.music.application.service.SearchMusicCategoryCacheService;
import whispy_server.whispy.domain.music.application.service.SearchMusicCategoryService;
import whispy_server.whispy.domain.music.model.type.MusicCategory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * SearchMusicCategoryService의 단위 테스트 클래스
 *
 * 카테고리별 음악 검색 서비스의 다양한 시나리오를 검증합니다.
 * 카테고리 기반 음악 검색 로직을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SearchMusicCategoryService 테스트")
class SearchMusicCategoryServiceTest {

    @InjectMocks
    private SearchMusicCategoryService searchMusicCategoryService;

    @Mock
    private SearchMusicCategoryCacheService searchMusicCategoryCacheService;

    @Test
    @DisplayName("카테고리로 음악을 검색할 수 있다")
    void whenSearchingByCategory_thenReturnsMusics() {
        // given
        MusicCategory category = MusicCategory.NATURE;
        Pageable pageable = PageRequest.of(0, 10);

        List<MusicSearchResponse> responses = List.of(
                new MusicSearchResponse(1L, "빗소리 1", "Nature Sounds", "편안한 빗소리", "http://example.com/music1.mp3", 180, MusicCategory.NATURE, "http://example.com/cover1.jpg", "http://example.com/video1.mp4"),
                new MusicSearchResponse(2L, "빗소리 2", "Nature Sounds", "부드러운 빗소리", "http://example.com/music2.mp3", 200, MusicCategory.NATURE, "http://example.com/cover2.jpg", "http://example.com/video2.mp4")
        );
        Page<MusicSearchResponse> musicPage = new PageImpl<>(responses, pageable, responses.size());

        given(searchMusicCategoryCacheService.searchByMusicCategory(category, pageable))
                .willReturn(MusicCategorySearchCacheValue.from(musicPage));

        // when
        Page<MusicSearchResponse> result = searchMusicCategoryService.searchByMusicCategory(category, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        verify(searchMusicCategoryCacheService).searchByMusicCategory(category, pageable);
    }

    @Test
    @DisplayName("해당 카테고리의 음악이 없을 때 빈 페이지를 반환한다")
    void whenNoMusicsInCategory_thenReturnsEmptyPage() {
        // given
        MusicCategory category = MusicCategory.NATURE;
        Pageable pageable = PageRequest.of(0, 10);
        Page<MusicSearchResponse> emptyPage = Page.empty(pageable);

        given(searchMusicCategoryCacheService.searchByMusicCategory(category, pageable))
                .willReturn(MusicCategorySearchCacheValue.from(emptyPage));

        // when
        Page<MusicSearchResponse> result = searchMusicCategoryService.searchByMusicCategory(category, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
    }
}
