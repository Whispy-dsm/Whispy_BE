package whispy_server.whispy.domain.music.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface DeleteMusicUseCase {
    void execute(Long id);
}
