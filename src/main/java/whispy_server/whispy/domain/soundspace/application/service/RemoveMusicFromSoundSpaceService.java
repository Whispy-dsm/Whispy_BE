package whispy_server.whispy.domain.soundspace.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.soundspace.application.port.in.RemoveMusicFromSoundSpaceUseCase;
import whispy_server.whispy.domain.soundspace.application.port.out.DeleteSoundSpaceMusicPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;

@Service
@RequiredArgsConstructor
public class RemoveMusicFromSoundSpaceService implements RemoveMusicFromSoundSpaceUseCase {

    private final DeleteSoundSpaceMusicPort deleteSoundSpaceMusicPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Transactional
    @Override
    public void execute(Long musicId) {
        User currentUser = userFacadeUseCase.currentUser();
        deleteSoundSpaceMusicPort.deleteByUserIdAndMusicId(currentUser.id(), musicId);
    }
}
