package whispy_server.whispy.domain.like.application.port.out;

public interface DeleteMusicLikePort {
    void deleteByUserIdAndMusicId(Long userId, Long musicId);
}
