package whispy_server.whispy.domain.music.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.music.application.port.in.DeleteMusicUseCase;
import whispy_server.whispy.domain.music.application.port.out.MusicPort;
import whispy_server.whispy.global.annotation.UseCase;
import whispy_server.whispy.global.exception.domain.music.MusicNotFoundException;

@Service
@RequiredArgsConstructor
public class DeleteMusicService implements DeleteMusicUseCase {

    private final MusicPort musicPort;

    @Transactional
    @Override
    public void execute(Long id) {
        if (!musicPort.existsById(id)) {
            throw MusicNotFoundException.EXCEPTION;
        }
        
        musicPort.deleteById(id);
    }
}
