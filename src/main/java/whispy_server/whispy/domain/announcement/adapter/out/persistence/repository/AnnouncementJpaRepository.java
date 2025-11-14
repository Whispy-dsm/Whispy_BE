package whispy_server.whispy.domain.announcement.adapter.out.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.announcement.adapter.out.entity.AnnouncementJpaEntity;
import whispy_server.whispy.domain.announcement.model.Announcement;

import java.util.List;

public interface AnnouncementJpaRepository extends JpaRepository<AnnouncementJpaEntity, Long> {
    Page<AnnouncementJpaEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
