package whispy_server.whispy.domain.soundspace.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.soundspace.application.port.in.RemoveMusicFromSoundSpaceUseCase;
import whispy_server.whispy.domain.soundspace.application.port.out.DeleteSoundSpaceMusicPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.annotation.UserAction;

/**
 * 현재 사용자의 사운드 스페이스에서 특정 음악을 제거하는 서비스.
 */
@Service
@RequiredArgsConstructor
public class RemoveMusicFromSoundSpaceService implements RemoveMusicFromSoundSpaceUseCase {

    private final DeleteSoundSpaceMusicPort deleteSoundSpaceMusicPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @UserAction("사운드 스페이스에서 음악 제거")
    @Transactional
    @Override
    public void execute(Long musicId) {
        User currentUser = userFacadeUseCase.currentUser();
        deleteSoundSpaceMusicPort.deleteByUserIdAndMusicId(currentUser.id(), musicId);
    }
}
