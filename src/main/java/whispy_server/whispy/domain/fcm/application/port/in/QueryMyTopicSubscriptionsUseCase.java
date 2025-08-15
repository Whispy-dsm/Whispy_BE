package whispy_server.whispy.domain.fcm.application.port.in;

import whispy_server.whispy.domain.fcm.adapter.in.web.dto.response.TopicSubscriptionResponse;
import whispy_server.whispy.domain.fcm.model.types.NotificationTopic;

import java.util.List;

public interface QueryMyTopicSubscriptionsUseCase {
    List<TopicSubscriptionResponse> execute();
}
