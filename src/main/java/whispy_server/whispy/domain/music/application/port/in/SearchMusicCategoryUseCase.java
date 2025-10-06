package whispy_server.whispy.domain.music.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.music.model.type.MusicCategory;
import whispy_server.whispy.domain.music.adapter.in.web.dto.response.MusicSearchResponse;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface SearchMusicCategoryUseCase {
    Page<MusicSearchResponse> searchByMusicCategory(MusicCategory category, Pageable pageable);
}

