package whispy_server.whispy.domain.soundspace.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.soundspace.adapter.in.web.dto.response.MusicInSoundSpaceCheckResponse;
import whispy_server.whispy.domain.soundspace.application.port.in.CheckMusicInSoundSpaceUseCase;
import whispy_server.whispy.domain.soundspace.application.port.out.QuerySoundSpaceMusicPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;

@Service
@RequiredArgsConstructor
public class CheckMusicInSoundSpaceService implements CheckMusicInSoundSpaceUseCase {

    private final QuerySoundSpaceMusicPort querySoundSpaceMusicPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Transactional(readOnly = true)
    @Override
    public MusicInSoundSpaceCheckResponse execute(Long musicId) {
        User currentUser = userFacadeUseCase.currentUser();
        boolean isInSoundSpace = querySoundSpaceMusicPort.existsByUserIdAndMusicId(currentUser.id(), musicId);

        return new MusicInSoundSpaceCheckResponse(isInSoundSpace);
    }
}
