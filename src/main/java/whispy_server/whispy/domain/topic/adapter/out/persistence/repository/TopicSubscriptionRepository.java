package whispy_server.whispy.domain.topic.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.topic.adapter.out.entity.TopicSubscriptionJpaEntity;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import java.util.List;
import java.util.Optional;

/**
 * 토픽 구독 레포지토리.
 *
 * 토픽 구독 엔티티에 대한 데이터베이스 접근을 제공합니다.
 */
public interface TopicSubscriptionRepository extends JpaRepository<TopicSubscriptionJpaEntity, Long> {

    /**
     * 이메일로 토픽 구독 목록을 조회합니다.
     *
     * @param email 사용자 이메일
     * @return 토픽 구독 엔티티 목록
     */
    List<TopicSubscriptionJpaEntity> findByEmail(String email);

    /**
     * 이메일과 토픽으로 토픽 구독을 조회합니다.
     *
     * @param email 사용자 이메일
     * @param topic 알림 토픽
     * @return 토픽 구독 엔티티 Optional
     */
    Optional<TopicSubscriptionJpaEntity> findByEmailAndTopic(String email, NotificationTopic topic);

    /**
     * 특정 토픽을 구독 중인 사용자 목록을 조회합니다.
     *
     * @param topic 알림 토픽
     * @return 구독 중인 사용자의 토픽 구독 엔티티 목록
     */
    List<TopicSubscriptionJpaEntity> findByTopicAndSubscribedTrue(NotificationTopic topic);

    /**
     * 이메일로 토픽 구독을 삭제합니다.
     *
     * @param email 사용자 이메일
     */
    void deleteByEmail(String email);
}
