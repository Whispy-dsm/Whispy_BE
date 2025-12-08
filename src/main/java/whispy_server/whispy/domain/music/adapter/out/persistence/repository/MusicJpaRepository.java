package whispy_server.whispy.domain.music.adapter.out.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.music.adapter.out.entity.MusicJpaEntity;
import whispy_server.whispy.domain.music.model.type.MusicCategory;

/**
 * 음악 JPA 레포지토리.
 *
 * MusicJpaEntity에 대한 데이터베이스 접근 기능을 제공합니다.
 */
public interface MusicJpaRepository extends JpaRepository<MusicJpaEntity, Long> {
    /**
     * 음악 제목에 포함된 키워드로 음악을 검색합니다.
     *
     * @param title 검색할 제목 키워드
     * @param pageable 페이지 정보
     * @return 검색된 음악 JPA 엔티티 페이지
     */
    Page<MusicJpaEntity> findByTitleContaining(String title, Pageable pageable);

    /**
     * 음악 카테고리로 음악을 검색합니다.
     *
     * @param category 검색할 음악 카테고리
     * @param pageable 페이지 정보
     * @return 해당 카테고리의 음악 JPA 엔티티 페이지
     */
    Page<MusicJpaEntity> findByCategory(MusicCategory category, Pageable pageable);
}
