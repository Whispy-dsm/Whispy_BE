package whispy_server.whispy.domain.fcm.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.fcm.adapter.out.entity.TopicSubscriptionJpaEntity;
import whispy_server.whispy.domain.fcm.model.types.NotificationTopic;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TopicSubscriptionRepository extends JpaRepository<TopicSubscriptionJpaEntity, UUID> {

    List<TopicSubscriptionJpaEntity> findByEmail(String email);

    Optional<TopicSubscriptionJpaEntity> findByEmailAndTopic(String email, NotificationTopic topic);

    List<TopicSubscriptionJpaEntity> findByTopicAndIsSubscribedTrue(NotificationTopic topic);
}
