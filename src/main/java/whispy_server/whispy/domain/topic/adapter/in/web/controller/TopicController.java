package whispy_server.whispy.domain.topic.adapter.in.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.TopicSubscriptionRequest;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.UpdateFcmTokenRequest;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.response.TopicSubscriptionResponse;
import whispy_server.whispy.domain.topic.application.port.in.*;
import whispy_server.whispy.global.document.api.topic.TopicApiDocument;

import java.util.List;

/**
 * 토픽 관리 REST 컨트롤러.
 *
 * FCM 토픽 구독 및 구독 취소, 구독 목록 조회 기능을 제공합니다.
 */
@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController implements TopicApiDocument {

    private final SubscriptionTopicUseCase subscriptionTopicUseCase;
    private final UnSubscribeTopicUseCase unSubscribeTopicUseCase;
    private final QueryMyTopicSubscriptionsUseCase queryMyTopicSubscriptionsUseCase;

    /**
     * 토픽을 구독합니다.
     *
     * @param request 토픽 구독 요청
     */
    @PostMapping("/subscribe")
    public void subscribeTopic(@RequestBody TopicSubscriptionRequest request) {
        subscriptionTopicUseCase.execute(request);
    }

    /**
     * 토픽 구독을 취소합니다.
     *
     * @param request 토픽 구독 취소 요청
     */
    @PostMapping("/unsubscribe")
    public void unsubscribeTopic(@RequestBody TopicSubscriptionRequest request) {
        unSubscribeTopicUseCase.execute(request);
    }

    /**
     * 내 토픽 구독 목록을 조회합니다.
     *
     * @return 토픽 구독 목록
     */
    @GetMapping("/subscriptions")
    public List<TopicSubscriptionResponse> getMySubscriptions() {
        return queryMyTopicSubscriptionsUseCase.execute();
    }

}
