package whispy_server.whispy.domain.fcm.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

import java.util.UUID;

@UseCase
public interface MarkNotificationAsReadUseCase {
    void execute(UUID notificationId);
}
