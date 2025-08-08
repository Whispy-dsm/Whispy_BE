package whispy_server.whispy.domain.fcm.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

import java.util.Map;

@UseCase
public interface BroadCastToAllUsersUseCase {
    void execute(String title, String body, Map<String, String> data);
}
