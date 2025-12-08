package whispy_server.whispy.domain.topic.application.port.out;

import whispy_server.whispy.domain.topic.model.TopicSubscription;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import java.util.List;
import java.util.Optional;

/**
 * 토픽 구독 조회 포트.
 *
 * 토픽 구독 정보를 조회하는 아웃바운드 포트입니다.
 */
public interface QueryTopicSubscriptionPort {
    /**
     * 이메일로 토픽 구독 목록을 조회합니다.
     *
     * @param email 사용자 이메일
     * @return 토픽 구독 목록
     */
    List<TopicSubscription> findByEmail(String email);

    /**
     * 이메일과 토픽으로 토픽 구독을 조회합니다.
     *
     * @param email 사용자 이메일
     * @param topic 알림 토픽
     * @return 토픽 구독 Optional
     */
    Optional<TopicSubscription> findByEmailAndTopic(String email, NotificationTopic topic);

    /**
     * 특정 토픽을 구독 중인 사용자 목록을 조회합니다.
     *
     * @param topic 알림 토픽
     * @return 구독 중인 사용자의 토픽 구독 목록
     */
    List<TopicSubscription> findSubscribedUserByTopic(NotificationTopic topic);
}
