package whispy_server.whispy.domain.topic.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface InitializeTopicsUseCase {
    void execute(String email, String fcmToken);
}
