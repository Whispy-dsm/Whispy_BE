package whispy_server.whispy.domain.sleepsession.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface DeleteSleepSessionUseCase {
    void execute(Long focusSessionId);
}
