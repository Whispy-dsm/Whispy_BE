package whispy_server.whispy.domain.topic.application.port.in;

import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface AddNewTopicForAllUsersUseCase {
    void execute(NotificationTopic newTopic, boolean defaultSubscribed);
}
