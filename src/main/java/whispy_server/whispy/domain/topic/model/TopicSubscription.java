package whispy_server.whispy.domain.topic.model;

import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.global.annotation.Aggregate;

/**
 * 토픽 구독 도메인 모델 (애그리게잇).
 *
 * FCM 토픽 구독 정보를 담고 있는 도메인 모델입니다.
 *
 * @param id 구독 ID
 * @param email 사용자 이메일
 * @param topic 알림 토픽
 * @param subscribed 구독 여부
 */
@Aggregate
public record TopicSubscription(
        Long id,
        String email,
        NotificationTopic topic,
        boolean subscribed

) {
    /**
     * 구독 상태를 업데이트합니다.
     *
     * @param isSubscribed 새로운 구독 상태
     * @return 업데이트된 토픽 구독
     */
    public TopicSubscription updateSubscription(boolean isSubscribed) {
        return new TopicSubscription(
                this.id,
                this.email,
                this.topic,
                subscribed
        );
    }
}
