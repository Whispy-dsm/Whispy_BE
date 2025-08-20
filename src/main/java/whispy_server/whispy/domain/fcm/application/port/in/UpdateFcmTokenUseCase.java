package whispy_server.whispy.domain.fcm.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface UpdateFcmTokenUseCase {
    void execute(String fcmToken);
}
