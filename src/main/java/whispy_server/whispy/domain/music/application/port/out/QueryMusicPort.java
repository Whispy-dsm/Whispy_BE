package whispy_server.whispy.domain.music.application.port.out;

import whispy_server.whispy.domain.music.model.Music;
import java.util.Optional;

public interface QueryMusicPort {
    Optional<Music> findById(Long id);
    boolean existsById(Long id);
}
