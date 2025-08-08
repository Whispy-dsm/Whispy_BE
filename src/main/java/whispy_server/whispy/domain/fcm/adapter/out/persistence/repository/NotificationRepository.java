package whispy_server.whispy.domain.fcm.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<NotificationJpaEntity, UUID> {
}
