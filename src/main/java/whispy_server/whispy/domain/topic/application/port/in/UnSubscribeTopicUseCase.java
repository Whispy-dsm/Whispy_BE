package whispy_server.whispy.domain.topic.application.port.in;

import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.TopicSubscriptionRequest;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 토픽 구독 취소 유스케이스.
 *
 * 사용자가 특정 토픽의 구독을 취소하도록 처리합니다.
 */
@UseCase
public interface UnSubscribeTopicUseCase {
    /**
     * 토픽 구독을 취소합니다.
     *
     * @param request 토픽 구독 취소 요청
     */
    void execute(TopicSubscriptionRequest request);
}
