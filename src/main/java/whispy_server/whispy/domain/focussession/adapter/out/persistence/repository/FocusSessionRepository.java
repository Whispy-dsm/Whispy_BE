package whispy_server.whispy.domain.focussession.adapter.out.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.focussession.adapter.out.entity.FocusSessionJpaEntity;

import java.util.Optional;

public interface FocusSessionRepository extends JpaRepository<FocusSessionJpaEntity, Long> {
    Page<FocusSessionJpaEntity> findByUserIdOrderByStartedAtDesc(Long userId, Pageable pageable);
    Optional<FocusSessionJpaEntity> findByIdAndUserId(Long id, Long userId);
}
