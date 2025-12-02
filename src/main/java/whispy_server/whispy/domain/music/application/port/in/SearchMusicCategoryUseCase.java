package whispy_server.whispy.domain.music.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.music.model.type.MusicCategory;
import whispy_server.whispy.domain.music.adapter.in.web.dto.response.MusicSearchResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 음악 카테고리 검색 유스케이스.
 *
 * 카테고리별로 음악을 검색하는 인바운드 포트입니다.
 */
@UseCase
public interface SearchMusicCategoryUseCase {
    /**
     * 지정된 카테고리의 음악을 검색합니다.
     *
     * @param category 검색할 음악 카테고리
     * @param pageable 페이지 정보
     * @return 검색된 음악 페이지
     */
    Page<MusicSearchResponse> searchByMusicCategory(MusicCategory category, Pageable pageable);
}

