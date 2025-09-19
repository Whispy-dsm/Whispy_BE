package whispy_server.whispy.domain.music.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.music.adapter.in.web.dto.request.UpdateMusicRequest;
import whispy_server.whispy.domain.music.application.port.in.UpdateMusicUseCase;
import whispy_server.whispy.domain.music.application.port.out.MusicPort;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.global.annotation.UseCase;
import whispy_server.whispy.global.exception.domain.music.MusicNotFoundException;

@Service
@RequiredArgsConstructor
public class UpdateMusicService implements UpdateMusicUseCase {

    private final MusicPort musicPort;

    @Transactional
    @Override
    public void execute(Long id, UpdateMusicRequest request) {
        Music existingMusic = musicPort.findById(id)
                .orElseThrow(() -> MusicNotFoundException.EXCEPTION);

        Music updatedMusic = existingMusic.update(request);
        musicPort.save(updatedMusic);
    }
}
