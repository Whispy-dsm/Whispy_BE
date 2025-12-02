package whispy_server.whispy.domain.topic.application.port.out;

/**
 * 토픽 구독 통합 포트.
 *
 * 토픽 구독 조회, 저장, 삭제 포트를 통합한 인터페이스입니다.
 */
public interface TopicSubscriptionPort extends QueryTopicSubscriptionPort, SaveTopicSubscriptionPort, DeleteTopicSubscriptionPort {
}
