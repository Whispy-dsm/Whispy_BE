package whispy_server.whispy.domain.notification.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.notification.adapter.out.entity.NotificationJpaEntity;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<NotificationJpaEntity, UUID> {

    List<NotificationJpaEntity> findByEmailOrderByCreatedAtDesc(String email);

    List<NotificationJpaEntity> findByEmailAndIsReadFalseOrderByCreatedAtDesc(String email);

    List<NotificationJpaEntity> findByEmailAndIsReadFalse(String email);

    int countByEmailAndIsReadFalse(String email);
}
