package whispy_server.whispy.domain.topic.adapter.in.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.TopicSubscriptionRequest;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.UpdateFcmTokenRequest;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.response.TopicSubscriptionResponse;
import whispy_server.whispy.domain.topic.application.port.in.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {

    private final SubscriptionTopicUseCase subscriptionTopicUseCase;
    private final UnSubscribeTopicUseCase unSubscribeTopicUseCase;
    private final QueryMyTopicSubscriptionsUseCase queryMyTopicSubscriptionsUseCase;

    @PostMapping("/subscribe")
    public void subscribeTopic(@RequestBody TopicSubscriptionRequest request) {
        subscriptionTopicUseCase.execute(request);
    }

    @PostMapping("/unsubscribe")
    public void unsubscribeTopic(@RequestBody TopicSubscriptionRequest request) {
        unSubscribeTopicUseCase.execute(request);
    }

    @GetMapping("/subscriptions")
    public List<TopicSubscriptionResponse> getMySubscriptions() {
        return queryMyTopicSubscriptionsUseCase.execute();
    }

}
