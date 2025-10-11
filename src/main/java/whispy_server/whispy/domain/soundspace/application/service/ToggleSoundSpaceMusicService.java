package whispy_server.whispy.domain.soundspace.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.music.application.port.out.QueryMusicPort;
import whispy_server.whispy.domain.soundspace.application.port.in.ToggleSoundSpaceMusicUseCase;
import whispy_server.whispy.domain.soundspace.application.port.out.DeleteSoundSpaceMusicPort;
import whispy_server.whispy.domain.soundspace.application.port.out.QuerySoundSpaceMusicPort;
import whispy_server.whispy.domain.soundspace.application.port.out.SaveSoundSpaceMusicPort;
import whispy_server.whispy.domain.soundspace.model.SoundSpaceMusic;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.exception.domain.music.MusicNotFoundException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ToggleSoundSpaceMusicService implements ToggleSoundSpaceMusicUseCase {

    private final QuerySoundSpaceMusicPort querySoundSpaceMusicPort;
    private final SaveSoundSpaceMusicPort saveSoundSpaceMusicPort;
    private final DeleteSoundSpaceMusicPort deleteSoundSpaceMusicPort;
    private final QueryMusicPort queryMusicPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public void execute(Long musicId) {
        User currentUser = userFacadeUseCase.currentUser();
        Long userId = currentUser.id();

        if(!queryMusicPort.existsById(musicId)) {
            throw MusicNotFoundException.EXCEPTION;
        }

        boolean exists = querySoundSpaceMusicPort.existsByUserIdAndMusicId(userId, musicId);

        if(exists) {
            deleteSoundSpaceMusicPort.deleteByUserIdAndMusicId(userId, musicId);
        } else {
            SoundSpaceMusic soundSpaceMusic = new SoundSpaceMusic(
                    null,
                    userId,
                    musicId,
                    LocalDateTime.now()
            );
            saveSoundSpaceMusicPort.save(soundSpaceMusic);
        }
    }
}
