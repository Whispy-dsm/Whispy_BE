package whispy_server.whispy.domain.soundspace.application.port.out;

import whispy_server.whispy.domain.soundspace.model.SoundSpaceMusic;

public interface SaveSoundSpaceMusicPort {
    void save(SoundSpaceMusic soundSpaceMusic);
}
