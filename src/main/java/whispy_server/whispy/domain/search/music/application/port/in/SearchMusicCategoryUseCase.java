package whispy_server.whispy.domain.search.music.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.music.model.type.MusicCategory;

public interface SearchMusicCategoryUseCase {
    Page<Music> searchByMusicCategory(MusicCategory category, Pageable pageable);
}
