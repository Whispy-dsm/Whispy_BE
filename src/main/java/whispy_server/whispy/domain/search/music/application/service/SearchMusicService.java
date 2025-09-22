package whispy_server.whispy.domain.search.music.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.search.music.application.port.in.SearchMusicUseCase;
import whispy_server.whispy.domain.search.music.application.port.out.SearchMusicPort;
import whispy_server.whispy.domain.music.model.Music;

@Service
@RequiredArgsConstructor
public class SearchMusicService implements SearchMusicUseCase {

    private final SearchMusicPort searchMusicPort;

    @Override
    public Page<Music> searchMusic(String keyword, Pageable pageable) {
        return searchMusicPort.searchByKeyword(keyword, pageable);
    }
}
