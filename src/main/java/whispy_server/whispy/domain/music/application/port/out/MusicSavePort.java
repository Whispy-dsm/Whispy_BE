package whispy_server.whispy.domain.music.application.port.out;

import whispy_server.whispy.domain.music.model.Music;

public interface MusicSavePort {
    void save(Music music);
}
