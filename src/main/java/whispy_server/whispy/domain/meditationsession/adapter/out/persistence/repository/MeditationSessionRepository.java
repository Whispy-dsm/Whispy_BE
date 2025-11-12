package whispy_server.whispy.domain.meditationsession.adapter.out.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.meditationsession.adapter.out.entity.MeditationSessionJpaEntity;

import java.util.Optional;

public interface MeditationSessionRepository extends JpaRepository<MeditationSessionJpaEntity, Long> {
    Page<MeditationSessionJpaEntity> findByUserIdOrderByStartedAtDesc(Long userId, Pageable pageable);
    Optional<MeditationSessionJpaEntity> findByIdAndUserId(Long id, Long userId);
}
