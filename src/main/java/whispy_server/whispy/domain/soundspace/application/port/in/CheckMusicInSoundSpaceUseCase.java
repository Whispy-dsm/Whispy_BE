package whispy_server.whispy.domain.soundspace.application.port.in;

import whispy_server.whispy.domain.soundspace.adapter.in.web.dto.response.MusicInSoundSpaceCheckResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 특정 음악이 사운드 스페이스에 존재하는지 확인하는 유스케이스.
 */
@UseCase
public interface CheckMusicInSoundSpaceUseCase {
    MusicInSoundSpaceCheckResponse execute(Long musicId);
}
