package whispy_server.whispy.domain.music.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.music.adapter.in.web.dto.response.MusicDetailResponse;
import whispy_server.whispy.domain.music.adapter.in.web.dto.response.MusicSearchResponse;
import whispy_server.whispy.domain.music.application.port.in.GetMusicDetailUseCase;
import whispy_server.whispy.domain.music.application.port.in.SearchMusicCategoryUseCase;
import whispy_server.whispy.domain.music.application.port.in.SearchMusicTitleUseCase;
import whispy_server.whispy.domain.music.model.type.MusicCategory;
import whispy_server.whispy.global.document.api.music.MusicApiDocument;

/**
 * 음악 REST 컨트롤러.
 *
 * 음악 검색 및 조회 기능을 제공하는 인바운드 어댑터입니다.
 */
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class MusicController implements MusicApiDocument {

    private final SearchMusicTitleUseCase searchMusicTitleUseCase;
    private final SearchMusicCategoryUseCase searchMusicCategoryUseCase;
    private final GetMusicDetailUseCase getMusicDetailUseCase;

    /**
     * 제목으로 음악을 검색합니다.
     *
     * @param keyword 검색 키워드
     * @param pageable 페이지 정보
     * @return 음악 검색 결과 페이지
     */
    @GetMapping("/music")
    public Page<MusicSearchResponse> searchMusic(@RequestParam String keyword, Pageable pageable) {
        return searchMusicTitleUseCase.searchMusic(keyword, pageable);
    }

    /**
     * 카테고리로 음악을 검색합니다.
     *
     * @param musicCategory 음악 카테고리
     * @param pageable 페이지 정보
     * @return 음악 검색 결과 페이지
     */
    @GetMapping("/music/category")
    public Page<MusicSearchResponse> searchByMusicCategory(@RequestParam MusicCategory musicCategory, Pageable pageable) {
        return searchMusicCategoryUseCase.searchByMusicCategory(musicCategory, pageable);
    }

    /**
     * 음악 상세 정보를 조회합니다.
     *
     * @param id 조회할 음악 ID
     * @return 음악 상세 정보
     */
    @GetMapping("/{id}")
    public MusicDetailResponse getMusicDetail(@PathVariable Long id) {
        return getMusicDetailUseCase.execute(id);
    }
}