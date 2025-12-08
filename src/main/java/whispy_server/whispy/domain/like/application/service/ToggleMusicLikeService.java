package whispy_server.whispy.domain.like.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.like.application.port.in.ToggleMusicLikeUseCase;
import whispy_server.whispy.domain.like.application.port.out.DeleteMusicLikePort;
import whispy_server.whispy.domain.like.application.port.out.QueryMusicLikePort;
import whispy_server.whispy.domain.like.application.port.out.SaveMusicLikePort;
import whispy_server.whispy.domain.like.model.MusicLike;
import whispy_server.whispy.domain.music.application.port.out.QueryMusicPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.exception.domain.music.MusicNotFoundException;

/**
 * 현재 사용자의 음악 좋아요 상태를 조회 후 저장/삭제하여 토글하는 서비스.
 * 음악 존재 여부를 검증한 뒤 원자적으로 상태를 반전시킨다.
 */
@Service
@RequiredArgsConstructor
public class ToggleMusicLikeService implements ToggleMusicLikeUseCase {

    private final SaveMusicLikePort saveMusicLikePort;
    private final QueryMusicLikePort queryMusicLikePort;
    private final DeleteMusicLikePort deleteMusicLikePort;
    private final UserFacadeUseCase userFacadeUseCase;
    private final QueryMusicPort queryMusicPort;

    @Transactional
    @Override
    public void execute(Long musicId) {
        User currentUser = userFacadeUseCase.currentUser();
        Long userId = currentUser.id();

        if(!queryMusicPort.existsById(musicId)) {
            throw MusicNotFoundException.EXCEPTION;
        }

        if(queryMusicLikePort.existsByUserIdAndMusicId(userId, musicId)) {
            deleteMusicLikePort.deleteByUserIdAndMusicId(userId, musicId);
        } else {
            MusicLike musicLike = new MusicLike(null, userId, musicId);
            saveMusicLikePort.save(musicLike);
        }
    }
}
