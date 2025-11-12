package whispy_server.whispy.domain.meditationsession.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface DeleteMeditationSessionUseCase {
    void execute(Long meditationSessionId);
}
