package whispy_server.whispy.domain.search.music.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.music.model.Music;

public interface SearchMusicTitleUseCase {
    Page<Music> searchMusic(String keyword, Pageable pageable);
}
