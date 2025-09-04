package whispy_server.whispy.domain.notification.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

import java.util.UUID;

@UseCase
public interface DeleteNotificationUseCase {
    void execute(UUID notificationId);
}