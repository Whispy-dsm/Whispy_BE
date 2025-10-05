package whispy_server.whispy.domain.search.music.application.port.out;

import whispy_server.whispy.domain.music.model.Music;

public interface IndexMusicPort {
    void indexMusic(Music music);
}
