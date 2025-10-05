package whispy_server.whispy.domain.search.music.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.music.model.type.MusicCategory;
import whispy_server.whispy.domain.search.music.adapter.in.web.dto.response.MusicSearchResponse;

public interface SearchMusicCategoryUseCase {
    Page<MusicSearchResponse> searchByMusicCategory(MusicCategory category, Pageable pageable);
}
