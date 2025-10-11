package whispy_server.whispy.domain.soundspace.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.soundspace.adapter.out.entity.SoundSpaceMusicJpaEntity;

import java.util.List;

public interface SoundSpaceMusicJpaRepository extends JpaRepository<SoundSpaceMusicJpaEntity, Long> {

    boolean existsByUserIdAndMusicId(Long userId, Long musicId);

    void deleteByUserIdAndMusicId(Long userId, Long musicId);

    List<SoundSpaceMusicJpaEntity> findAllByUserIdOrderByAddedAtDesc(Long userId);
}
