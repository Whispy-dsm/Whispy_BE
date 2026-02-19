package whispy_server.whispy.domain.music.application.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.music.adapter.in.web.dto.response.MusicSearchResponse;

/**
 * 음악 카테고리 검색 캐시 저장용 값 객체.
 */
public record MusicCategorySearchCacheValue(
        List<MusicSearchResponse> content,
        long totalElements
) {

    public static MusicCategorySearchCacheValue from(Page<MusicSearchResponse> page) {
        return new MusicCategorySearchCacheValue(page.getContent(), page.getTotalElements());
    }

    public Page<MusicSearchResponse> toPage(Pageable pageable) {
        return new PageImpl<>(content, pageable, totalElements);
    }
}
