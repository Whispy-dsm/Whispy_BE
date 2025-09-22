package whispy_server.whispy.domain.search.music.adapter.in;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.search.music.application.port.in.SearchMusicUseCase;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class MusicSearchController {

    private final SearchMusicUseCase searchMusicUseCase;

    @GetMapping("/music")
    public Page<Music> searchMusic(@RequestParam String keyword, Pageable pageable) {
        return searchMusicUseCase.searchMusic(keyword, pageable);
    }
}
