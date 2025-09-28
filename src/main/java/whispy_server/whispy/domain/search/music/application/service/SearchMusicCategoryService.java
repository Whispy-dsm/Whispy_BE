package whispy_server.whispy.domain.search.music.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.music.model.type.MusicCategory;
import whispy_server.whispy.domain.search.music.application.port.in.SearchMusicCategoryUseCase;
import whispy_server.whispy.domain.search.music.application.port.out.SearchMusicPort;

@Service
@RequiredArgsConstructor
public class SearchMusicCategoryService implements SearchMusicCategoryUseCase {

    private final SearchMusicPort searchMusicPort;

    @Override
    public Page<Music> searchByMusicCategory(MusicCategory category, Pageable pageable) {
        return searchMusicPort.searchByCategory(category, pageable);
    }
}
