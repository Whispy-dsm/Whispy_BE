package whispy_server.whispy.domain.like.application.port.out;

import whispy_server.whispy.domain.like.model.MusicLike;

/**
 * 좋아요 정보를 저장하는 포트.
 */
public interface SaveMusicLikePort {
    void save(MusicLike musicLike);
}
