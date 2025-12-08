package whispy_server.whispy.domain.announcement.adapter.out.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.announcement.adapter.out.entity.AnnouncementJpaEntity;

/**
 * 공지사항 JPA 레포지토리.
 *
 * AnnouncementJpaEntity에 대한 데이터베이스 접근 기능을 제공합니다.
 */
public interface AnnouncementJpaRepository extends JpaRepository<AnnouncementJpaEntity, Long> {
    /**
     * 모든 공지사항을 생성일시 기준 내림차순으로 페이지네이션하여 조회합니다.
     *
     * @param pageable 페이지 정보
     * @return 공지사항 JPA 엔티티 페이지
     */
    Page<AnnouncementJpaEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
