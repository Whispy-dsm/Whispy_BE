package whispy_server.whispy.domain.like.application.port.out;

import whispy_server.whispy.domain.like.model.MusicLike;

public interface SaveMusicLikePort {
    void save(MusicLike musicLike);
}
