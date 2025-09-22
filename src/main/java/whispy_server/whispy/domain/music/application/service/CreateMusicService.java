package whispy_server.whispy.domain.music.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.music.adapter.in.web.dto.request.CreateMusicRequest;
import whispy_server.whispy.domain.music.application.port.in.CreateMusicUseCase;
import whispy_server.whispy.domain.music.application.port.out.MusicPort;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.search.music.application.port.out.SearchMusicPort;
import whispy_server.whispy.global.annotation.UseCase;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateMusicService implements CreateMusicUseCase {

    private final MusicPort musicPort;
    private final SearchMusicPort searchMusicPort;

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
        
        Music savedMusic = musicPort.save(music);

        try {
            searchMusicPort.indexMusic(savedMusic);
        } catch (Exception e) {
            log.warn("Failed to index music: {}", e.getMessage());
        }
    }
}
