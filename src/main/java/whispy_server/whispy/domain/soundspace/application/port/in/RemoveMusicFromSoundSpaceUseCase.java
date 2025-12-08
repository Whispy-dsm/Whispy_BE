package whispy_server.whispy.domain.soundspace.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

/**
 * 사운드 스페이스에서 음악을 제거하는 동작을 정의한다.
 */
@UseCase
public interface RemoveMusicFromSoundSpaceUseCase {
    void execute(Long musicId);
}
