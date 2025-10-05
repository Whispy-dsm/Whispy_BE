package whispy_server.whispy.domain.music.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.music.adapter.in.web.dto.request.UpdateMusicRequest;
import whispy_server.whispy.domain.music.application.port.in.UpdateMusicUseCase;
import whispy_server.whispy.domain.music.application.port.out.MusicPort;
import whispy_server.whispy.domain.music.application.port.out.MusicSavePort;
import whispy_server.whispy.domain.music.application.port.out.QueryMusicPort;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.global.exception.domain.music.MusicNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateMusicService implements UpdateMusicUseCase {

    private final QueryMusicPort queryMusicPort;
    private final MusicSavePort musicSavePort;

    @Transactional
    @Override
    public void execute(UpdateMusicRequest request) {
        Music existingMusic = queryMusicPort.findById(request.id())
                .orElseThrow(() -> MusicNotFoundException.EXCEPTION);

        Music updatedMusic = existingMusic.update(request);
        musicSavePort.save(updatedMusic);
    }
}
