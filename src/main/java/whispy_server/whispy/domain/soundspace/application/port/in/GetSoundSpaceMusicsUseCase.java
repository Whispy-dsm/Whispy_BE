package whispy_server.whispy.domain.soundspace.application.port.in;

import whispy_server.whispy.domain.soundspace.adapter.in.web.dto.response.SoundSpaceMusicResponse;
import whispy_server.whispy.global.annotation.UseCase;

import java.util.List;

@UseCase
public interface GetSoundSpaceMusicsUseCase {
    List<SoundSpaceMusicResponse> execute();
}
