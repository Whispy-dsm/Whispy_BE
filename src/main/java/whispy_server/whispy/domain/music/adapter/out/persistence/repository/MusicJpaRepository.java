package whispy_server.whispy.domain.music.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.music.adapter.out.entity.MusicJpaEntity;

public interface MusicJpaRepository extends JpaRepository<MusicJpaEntity, Long> {
}
