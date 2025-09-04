package whispy_server.whispy.domain.topic.application.port.in;

import whispy_server.whispy.domain.topic.adapter.in.web.dto.response.TopicSubscriptionResponse;

import java.util.List;

public interface QueryMyTopicSubscriptionsUseCase {
    List<TopicSubscriptionResponse> execute();
}
