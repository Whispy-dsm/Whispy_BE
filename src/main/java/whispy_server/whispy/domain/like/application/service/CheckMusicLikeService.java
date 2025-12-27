package whispy_server.whispy.domain.like.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.like.application.port.in.CheckMusicLikeUseCase;
import whispy_server.whispy.domain.like.application.port.out.QueryMusicLikePort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;

/**
 * 사용자의 음악 좋아요 여부를 확인하는 서비스.
 */
@Service
@RequiredArgsConstructor
public class CheckMusicLikeService implements CheckMusicLikeUseCase {

    private final QueryMusicLikePort queryMusicLikePort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Transactional(readOnly = true)
    @Override
    public boolean execute(Long musicId) {
        User currentUser = userFacadeUseCase.currentUser();
        return queryMusicLikePort.existsByUserIdAndMusicId(currentUser.id(), musicId);
    }
}
