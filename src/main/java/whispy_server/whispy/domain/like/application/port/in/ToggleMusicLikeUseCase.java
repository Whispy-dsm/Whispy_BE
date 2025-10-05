package whispy_server.whispy.domain.like.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface ToggleMusicLikeUseCase {
    void execute(Long musicId);
}
