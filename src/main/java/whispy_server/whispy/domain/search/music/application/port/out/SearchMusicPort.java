package whispy_server.whispy.domain.search.music.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.music.model.Music;

public interface SearchMusicPort {
    Page<Music> searchByKeyword(String keyword, Pageable pageable);
    void indexMusic(Music music);
    void deleteFromIndex(Long musicId);
}
