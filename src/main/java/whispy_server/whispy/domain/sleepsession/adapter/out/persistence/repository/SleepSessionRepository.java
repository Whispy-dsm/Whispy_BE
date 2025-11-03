package whispy_server.whispy.domain.sleepsession.adapter.out.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.focussession.adapter.out.entity.FocusSessionJpaEntity;
import whispy_server.whispy.domain.sleepsession.adapter.out.entity.SleepSessionJpaEntity;

import java.util.Optional;

public interface SleepSessionRepository extends JpaRepository<SleepSessionJpaEntity, Long> {
    Page<SleepSessionJpaEntity> findByUserIdOrderByStartedAtDesc(Long userId, Pageable pageable);
    Optional<SleepSessionJpaEntity> findByIdAndUserId(Long id, Long userId);
}
