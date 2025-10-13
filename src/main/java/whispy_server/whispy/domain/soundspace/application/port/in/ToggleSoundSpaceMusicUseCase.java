package whispy_server.whispy.domain.soundspace.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface ToggleSoundSpaceMusicUseCase {
    void execute(Long musicId);
}
