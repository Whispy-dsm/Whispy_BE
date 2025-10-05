package whispy_server.whispy.domain.announcement.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.announcement.adapter.out.entity.AnnouncementJpaEntity;

public interface AnnouncementJpaRepository extends JpaRepository<AnnouncementJpaEntity, Long> {
}
