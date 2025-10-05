package whispy_server.whispy.domain.music.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.music.adapter.in.web.dto.response.MusicSearchResponse;

public interface SearchMusicTitleUseCase {
    Page<MusicSearchResponse> searchMusic(String keyword, Pageable pageable);
}
