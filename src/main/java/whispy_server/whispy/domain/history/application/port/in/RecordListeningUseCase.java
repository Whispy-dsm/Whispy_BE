package whispy_server.whispy.domain.history.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface RecordListeningUseCase {
    void execute(Long musicId);
}
