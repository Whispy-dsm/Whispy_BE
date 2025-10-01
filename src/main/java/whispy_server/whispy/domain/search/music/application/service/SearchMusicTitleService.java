package whispy_server.whispy.domain.search.music.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.search.music.adapter.in.web.dto.response.MusicSearchResponse;
import whispy_server.whispy.domain.search.music.application.port.in.SearchMusicTitleUseCase;
import whispy_server.whispy.domain.search.music.application.port.out.SearchMusicPort;
import whispy_server.whispy.domain.music.model.Music;

@Service
@RequiredArgsConstructor
public class SearchMusicTitleService implements SearchMusicTitleUseCase {

    private final SearchMusicPort searchMusicPort;

    @Override
    public Page<MusicSearchResponse> searchMusic(String keyword, Pageable pageable) {
        return searchMusicPort.searchByKeyword(keyword, pageable)
                .map(MusicSearchResponse::from);
    }
}
