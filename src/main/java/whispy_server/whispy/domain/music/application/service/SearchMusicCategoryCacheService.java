package whispy_server.whispy.domain.music.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.music.adapter.in.web.dto.response.MusicSearchResponse;
import whispy_server.whispy.domain.music.application.port.out.SearchMusicPort;
import whispy_server.whispy.domain.music.model.type.MusicCategory;
import whispy_server.whispy.global.config.redis.RedisConfig;

/**
 * 음악 카테고리 검색 캐시 전용 서비스.
 */
@Service
@RequiredArgsConstructor
public class SearchMusicCategoryCacheService {

    private final SearchMusicPort searchMusicPort;

    @Cacheable(
            value = RedisConfig.MUSIC_CATEGORY_SEARCH_CACHE,
            key = "#category.name() + ':' + #pageable.pageNumber + ':' + #pageable.pageSize + ':' + #pageable.sort.toString()",
            condition = "#pageable.pageNumber == 0",
            sync = true
    )
    public MusicCategorySearchCacheValue searchByMusicCategory(MusicCategory category, Pageable pageable) {
        return MusicCategorySearchCacheValue.from(
                searchMusicPort.searchByCategory(category, pageable)
                        .map(MusicSearchResponse::from)
        );
    }
}
