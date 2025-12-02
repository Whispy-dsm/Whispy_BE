package whispy_server.whispy.domain.soundspace.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.soundspace.adapter.out.entity.SoundSpaceMusicJpaEntity;

import java.util.List;

/**
 * 사운드 스페이스 음악 테이블에 접근하는 Spring Data JPA 저장소.
 */
public interface SoundSpaceMusicJpaRepository extends JpaRepository<SoundSpaceMusicJpaEntity, Long> {

    boolean existsByUserIdAndMusicId(Long userId, Long musicId);

    void deleteByUserIdAndMusicId(Long userId, Long musicId);

    void deleteAllByMusicId(Long musicId);

    void deleteByUserId(Long userId);

    List<SoundSpaceMusicJpaEntity> findAllByUserIdOrderByAddedAtDesc(Long userId);
}
