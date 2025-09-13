package whispy_server.whispy.domain.notification.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.notification.adapter.out.entity.NotificationJpaEntity;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationJpaEntity, Long> {

    @EntityGraph(attributePaths = {"data"})
    List<NotificationJpaEntity> findByEmailOrderByCreatedAtDesc(String email);
    @EntityGraph(attributePaths = {"data"})
    List<NotificationJpaEntity> findByEmailAndReadFalseOrderByCreatedAtDesc(String email);
    @EntityGraph(attributePaths = {"data"})
    List<NotificationJpaEntity> findByEmailAndReadFalse(String email);
    int countByEmailAndReadFalse(String email);
    void deleteAllByEmail(String email);
}
