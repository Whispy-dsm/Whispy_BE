package whispy_server.whispy.domain.topic.application.port.out;

import whispy_server.whispy.domain.topic.model.TopicSubscription;

import java.util.List;

/**
 * 토픽 구독 저장 포트.
 *
 * 토픽 구독 정보를 저장하는 아웃바운드 포트입니다.
 */
public interface SaveTopicSubscriptionPort {
    /**
     * 토픽 구독을 저장합니다.
     *
     * @param topicSubscription 토픽 구독
     * @return 저장된 토픽 구독
     */
    TopicSubscription save(TopicSubscription topicSubscription);

    /**
     * 토픽 구독 목록을 일괄 저장합니다.
     *
     * @param subscriptions 토픽 구독 목록
     * @return 저장된 토픽 구독 목록
     */
    List<TopicSubscription> saveAll(List<TopicSubscription> subscriptions);
}
