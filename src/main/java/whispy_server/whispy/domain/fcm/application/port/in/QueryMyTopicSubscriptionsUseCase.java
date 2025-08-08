package whispy_server.whispy.domain.fcm.application.port.in;

import whispy_server.whispy.domain.fcm.model.types.NotificationTopic;

import java.util.List;

public interface QueryMyTopicSubscriptionsUseCase {
    List<NotificationTopic> execute();
}
