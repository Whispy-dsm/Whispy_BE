package whispy_server.whispy.domain.sleepsession.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.sleepsession.adapter.out.entity.SleepSessionJpaEntity;

public interface SleepSessionRepository extends JpaRepository<SleepSessionJpaEntity, Long> {
}
