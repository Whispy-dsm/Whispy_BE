package whispy_server.whispy.domain.fcm.application.port.in;

import whispy_server.whispy.domain.fcm.model.types.NotificationTopic;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface SubscriptionTopicUseCase {
    void execute(NotificationTopic topic);
}
