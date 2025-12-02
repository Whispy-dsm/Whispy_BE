package whispy_server.whispy.domain.soundspace.application.port.in;

import whispy_server.whispy.domain.soundspace.adapter.in.web.dto.response.SoundSpaceMusicResponse;
import whispy_server.whispy.global.annotation.UseCase;

import java.util.List;

/**
 * 사운드 스페이스에 저장된 음악 목록을 반환하는 유스케이스다.
 */
@UseCase
public interface GetSoundSpaceMusicsUseCase {
    List<SoundSpaceMusicResponse> execute();
}
