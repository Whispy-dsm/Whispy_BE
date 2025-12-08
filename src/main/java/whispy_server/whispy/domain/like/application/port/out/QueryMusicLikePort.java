package whispy_server.whispy.domain.like.application.port.out;

import whispy_server.whispy.domain.like.adapter.out.dto.MusicLikeWithMusicDto;
import whispy_server.whispy.domain.like.model.MusicLike;

import java.util.List;
import java.util.Optional;

/**
 * 좋아요 데이터 조회를 위한 포트.
 */
public interface QueryMusicLikePort {
    boolean existsByUserIdAndMusicId(Long userId, Long musicId);
    List<MusicLike> findAllByUserId(Long userId);
    Optional<MusicLike> findByUserIdAndMusicId(Long userId, Long musicId);
    List<MusicLikeWithMusicDto> findLikedMusicsWithDetailByUserId(Long userId);

}
