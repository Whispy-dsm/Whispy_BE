package whispy_server.whispy.domain.music.application.port.in;

import whispy_server.whispy.domain.music.adapter.in.web.dto.request.UpdateMusicRequest;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface UpdateMusicUseCase {
    void execute(UpdateMusicRequest request);
}
