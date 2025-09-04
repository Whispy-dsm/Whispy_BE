package whispy_server.whispy.domain.topic.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.topic.adapter.out.entity.TopicSubscriptionJpaEntity;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TopicSubscriptionRepository extends JpaRepository<TopicSubscriptionJpaEntity, UUID> {

    List<TopicSubscriptionJpaEntity> findByEmail(String email);

    Optional<TopicSubscriptionJpaEntity> findByEmailAndTopic(String email, NotificationTopic topic);

    List<TopicSubscriptionJpaEntity> findByTopicAndSubscribedTrue(NotificationTopic topic);
}
