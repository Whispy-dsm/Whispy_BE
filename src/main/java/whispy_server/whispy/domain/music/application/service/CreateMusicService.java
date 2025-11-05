package whispy_server.whispy.domain.music.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.music.adapter.in.web.dto.request.CreateMusicRequest;
import whispy_server.whispy.domain.music.application.port.in.CreateMusicUseCase;
import whispy_server.whispy.domain.music.application.port.out.MusicSavePort;
import whispy_server.whispy.domain.music.model.Music;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateMusicService implements CreateMusicUseCase {

    private final MusicSavePort musicSavePort;

    @Transactional
    @Override
    public void execute(CreateMusicRequest request) {
        Music music = new Music(
                null,
                request.title(),
                request.filePath(),
                request.duration(),
                request.category(),
                request.bannerImageUrl()
        );

        musicSavePort.save(music);
    }
}

