package whispy_server.whispy.domain.music.adapter.out.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.music.adapter.out.entity.MusicJpaEntity;
import whispy_server.whispy.domain.music.model.type.MusicCategory;

public interface MusicJpaRepository extends JpaRepository<MusicJpaEntity, Long> {
    Page<MusicJpaEntity> findByTitleContaining(String title, Pageable pageable);
    Page<MusicJpaEntity> findByCategory(MusicCategory category, Pageable pageable);
}
