package whispy_server.whispy.domain.topic.application.port.in;

import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.TopicSubscriptionRequest;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 토픽 구독 유스케이스.
 *
 * 사용자가 특정 토픽을 구독하도록 처리합니다.
 */
@UseCase
public interface SubscriptionTopicUseCase {
    /**
     * 토픽을 구독합니다.
     *
     * @param request 토픽 구독 요청
     */
    void execute(TopicSubscriptionRequest request);
}
