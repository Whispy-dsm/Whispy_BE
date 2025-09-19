package whispy_server.whispy.domain.music.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.music.adapter.in.web.dto.request.CreateMusicRequest;
import whispy_server.whispy.domain.music.application.port.in.CreateMusicUseCase;
import whispy_server.whispy.domain.music.application.port.out.MusicPort;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.global.annotation.UseCase;

@Service
@RequiredArgsConstructor
public class CreateMusicService implements CreateMusicUseCase {

    private final MusicPort musicPort;

    @Transactional
    @Override
    public void execute(CreateMusicRequest request) {
        Music music = new Music(
                null,
                request.title(),
                request.filePath(),
                request.duration(),
                request.category()
        );
        
        musicPort.save(music);
    }
}
