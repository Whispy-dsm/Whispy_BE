package whispy_server.whispy.domain.music.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.music.adapter.in.web.dto.request.UpdateMusicRequest;
import whispy_server.whispy.domain.music.application.port.in.UpdateMusicUseCase;
import whispy_server.whispy.domain.music.application.port.out.MusicPort;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.search.music.application.port.out.IndexMusicPort;
import whispy_server.whispy.domain.search.music.application.port.out.SearchMusicPort;
import whispy_server.whispy.global.annotation.UseCase;
import whispy_server.whispy.global.exception.domain.music.MusicNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateMusicService implements UpdateMusicUseCase {

    private final MusicPort musicPort;
    private final IndexMusicPort indexMusicPort;

    @Transactional
    @Override
    public void execute(UpdateMusicRequest request) {
        Music existingMusic = musicPort.findById(request.id())
                .orElseThrow(() -> MusicNotFoundException.EXCEPTION);

        Music updatedMusic = existingMusic.update(request);
        Music savedMusic = musicPort.save(updatedMusic);

        try {
            indexMusicPort.indexMusic(savedMusic);
        } catch (Exception e) {
            log.warn("Failed to update music index: {}", e.getMessage());
        }
    }
}
