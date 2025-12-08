package whispy_server.whispy.domain.topic.application.port.in;

import whispy_server.whispy.domain.topic.adapter.in.web.dto.response.TopicSubscriptionResponse;

import java.util.List;

/**
 * 내 토픽 구독 목록 조회 유스케이스.
 *
 * 현재 사용자의 토픽 구독 목록을 조회합니다.
 */
public interface QueryMyTopicSubscriptionsUseCase {
    /**
     * 내 토픽 구독 목록을 조회합니다.
     *
     * @return 토픽 구독 목록
     */
    List<TopicSubscriptionResponse> execute();
}
