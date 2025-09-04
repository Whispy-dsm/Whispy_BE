package whispy_server.whispy.domain.notification.application.port.in;

import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationTopicSendRequest;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface BroadCastToAllUsersUseCase {
    void execute(NotificationTopicSendRequest request);
}
