package whispy_server.whispy.domain.music.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.music.adapter.in.web.dto.response.MusicSearchResponse;
import whispy_server.whispy.domain.music.application.port.in.SearchMusicCategoryUseCase;
import whispy_server.whispy.domain.music.application.port.out.SearchMusicPort;
import whispy_server.whispy.domain.music.model.type.MusicCategory;
import whispy_server.whispy.global.config.redis.RedisConfig;
import whispy_server.whispy.global.annotation.UserAction;

/**
 * 음악 카테고리 검색 서비스.
 *
 * 카테고리별로 음악을 검색하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class SearchMusicCategoryService implements SearchMusicCategoryUseCase {

    private final SearchMusicPort searchMusicPort;

    /**
     * 지정된 카테고리의 음악을 검색합니다.
     *
     * @param category 검색할 음악 카테고리
     * @param pageable 페이지 정보
     * @return 검색된 음악 페이지
     */
    @UserAction("음악 카테고리 검색")
    @Cacheable(
            value = RedisConfig.MUSIC_CATEGORY_SEARCH_CACHE,
            key = "#category.name() + ':' + #pageable.pageNumber + ':' + #pageable.pageSize + ':' + #pageable.sort.toString()",
            condition = "#pageable.pageNumber == 0",
            sync = true
    )
    @Override
    public Page<MusicSearchResponse> searchByMusicCategory(MusicCategory category, Pageable pageable) {
        return searchMusicPort.searchByCategory(category, pageable)
                .map(MusicSearchResponse::from);
    }
}
