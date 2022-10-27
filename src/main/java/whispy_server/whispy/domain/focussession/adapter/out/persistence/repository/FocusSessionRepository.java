package whispy_server.whispy.domain.focussession.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.focussession.adapter.out.entity.FocusSessionJpaEntity;

public interface FocusSessionRepository extends JpaRepository<FocusSessionJpaEntity, Long> {
}
