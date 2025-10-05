package whispy_server.whispy.domain.music.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.music.adapter.in.web.dto.response.MusicSearchResponse;
import whispy_server.whispy.domain.music.application.port.in.SearchMusicCategoryUseCase;
import whispy_server.whispy.domain.music.application.port.in.SearchMusicTitleUseCase;
import whispy_server.whispy.domain.music.model.type.MusicCategory;
import whispy_server.whispy.global.document.api.music.MusicSearchApiDocument;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class MusicSearchController implements MusicSearchApiDocument {

    private final SearchMusicTitleUseCase searchMusicTitleUseCase;
    private final SearchMusicCategoryUseCase searchMusicCategoryUseCase;

    @GetMapping("/music")
    public Page<MusicSearchResponse> searchMusic(@RequestParam String keyword, Pageable pageable) {
        return searchMusicTitleUseCase.searchMusic(keyword, pageable);
    }

    @GetMapping("/music/category")
    public Page<MusicSearchResponse> searchByMusicCategory(@RequestParam MusicCategory musicCategory, Pageable pageable) {
        return searchMusicCategoryUseCase.searchByMusicCategory(musicCategory, pageable);
    }
}