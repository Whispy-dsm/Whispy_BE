package whispy_server.whispy.domain.music.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.music.adapter.in.web.dto.response.MusicSearchResponse;
import whispy_server.whispy.domain.music.application.port.in.SearchMusicTitleUseCase;
import whispy_server.whispy.domain.music.application.port.out.SearchMusicPort;

/**
 * 음악 제목 검색 서비스.
 *
 * 제목으로 음악을 검색하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class SearchMusicTitleService implements SearchMusicTitleUseCase {

    private final SearchMusicPort searchMusicPort;

    /**
     * 제목 키워드로 음악을 검색합니다.
     *
     * @param keyword 검색할 제목 키워드
     * @param pageable 페이지 정보
     * @return 검색된 음악 페이지
     */
    @Override
    public Page<MusicSearchResponse> searchMusic(String keyword, Pageable pageable) {
        return searchMusicPort.searchByTitle(keyword, pageable)
                .map(MusicSearchResponse::from);
    }
}
