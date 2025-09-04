package whispy_server.whispy.domain.user.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface UpdateFcmTokenUseCase {
    void execute(String fcmToken);
}
