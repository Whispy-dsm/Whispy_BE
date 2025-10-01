package whispy_server.whispy.domain.like.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.like.adapter.out.entity.MusicLikeJpaEntity;

import java.util.List;
import java.util.Optional;

public interface MusicLikeJpaRepository extends JpaRepository<MusicLikeJpaEntity, Long> {
    boolean existsByUserIdAndMusicId(Long userId, Long musicId);
    void deleteByUserIdAndMusicId(Long userId, Long musicId);
    void deleteAllByMusicId(Long musicId);
    List<MusicLikeJpaEntity> findAllByUserId(Long userId);
    Optional<MusicLikeJpaEntity> findByUserIdAndMusicId(Long userId, Long musicId);
}
