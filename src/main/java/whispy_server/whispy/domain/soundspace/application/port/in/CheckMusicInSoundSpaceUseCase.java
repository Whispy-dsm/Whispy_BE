package whispy_server.whispy.domain.soundspace.application.port.in;

import whispy_server.whispy.domain.soundspace.adapter.in.web.dto.response.MusicInSoundSpaceCheckResponse;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface CheckMusicInSoundSpaceUseCase {
    MusicInSoundSpaceCheckResponse execute(Long musicId);
}