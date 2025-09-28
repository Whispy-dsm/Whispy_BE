package whispy_server.whispy.domain.search.music.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.music.model.type.MusicCategory;

public interface SearchMusicPort {
    Page<Music> searchByKeyword(String keyword, Pageable pageable);
    Page<Music> searchByCategory(MusicCategory category, Pageable pageable);
}
