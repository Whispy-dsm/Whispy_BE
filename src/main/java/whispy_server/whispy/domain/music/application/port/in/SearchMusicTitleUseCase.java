package whispy_server.whispy.domain.music.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.music.adapter.in.web.dto.response.MusicSearchResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 음악 제목 검색 유스케이스.
 *
 * 제목으로 음악을 검색하는 인바운드 포트입니다.
 */
@UseCase
public interface SearchMusicTitleUseCase {
    /**
     * 제목 키워드로 음악을 검색합니다.
     *
     * @param keyword 검색할 제목 키워드
     * @param pageable 페이지 정보
     * @return 검색된 음악 페이지
     */
    Page<MusicSearchResponse> searchMusic(String keyword, Pageable pageable);
}
