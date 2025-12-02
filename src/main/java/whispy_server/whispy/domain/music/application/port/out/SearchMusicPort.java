package whispy_server.whispy.domain.music.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.music.model.type.MusicCategory;

/**
 * 음악 검색 아웃바운드 포트.
 *
 * 음악 검색 기능을 정의하는 아웃바운드 포트입니다.
 */
public interface SearchMusicPort {
    /**
     * 제목으로 음악을 검색합니다.
     *
     * @param title 검색할 제목 키워드
     * @param pageable 페이지 정보
     * @return 검색된 음악 페이지
     */
    Page<Music> searchByTitle(String title, Pageable pageable);

    /**
     * 카테고리로 음악을 검색합니다.
     *
     * @param category 검색할 음악 카테고리
     * @param pageable 페이지 정보
     * @return 검색된 음악 페이지
     */
    Page<Music> searchByCategory(MusicCategory category, Pageable pageable);
}