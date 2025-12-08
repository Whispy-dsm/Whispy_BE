package whispy_server.whispy.domain.soundspace.application.port.out;

import whispy_server.whispy.domain.soundspace.model.SoundSpaceMusic;

/**
 * 사운드 스페이스 엔트리를 저장하는 포트.
 */
public interface SaveSoundSpaceMusicPort {
    void save(SoundSpaceMusic soundSpaceMusic);
}
