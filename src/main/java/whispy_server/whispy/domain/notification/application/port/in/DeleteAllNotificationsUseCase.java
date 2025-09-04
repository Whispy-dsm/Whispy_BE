package whispy_server.whispy.domain.notification.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface DeleteAllNotificationsUseCase {
    void execute();
}
