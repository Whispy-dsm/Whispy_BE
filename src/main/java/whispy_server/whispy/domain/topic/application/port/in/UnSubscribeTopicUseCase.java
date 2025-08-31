package whispy_server.whispy.domain.topic.application.port.in;

import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.TopicSubscriptionRequest;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface UnSubscribeTopicUseCase {
    void execute(TopicSubscriptionRequest request);
}
