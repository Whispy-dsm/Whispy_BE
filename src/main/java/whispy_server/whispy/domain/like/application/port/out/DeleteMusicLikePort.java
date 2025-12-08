package whispy_server.whispy.domain.like.application.port.out;

/**
 * 좋아요 엔티티 삭제 작업을 정의한 포트.
 */
public interface DeleteMusicLikePort {
    void deleteByUserIdAndMusicId(Long userId, Long musicId);
    void deleteAllByMusicId(Long musicId);
    void deleteByUserId(Long userId);
}
