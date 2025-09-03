package whispy_server.whispy.domain.notification.application.port.in;

import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationSendRequest;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface SendToDeviceTokensUseCase {
    void execute(NotificationSendRequest request);
}
