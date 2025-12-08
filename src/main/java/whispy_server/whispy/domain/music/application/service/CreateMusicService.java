package whispy_server.whispy.domain.music.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.music.adapter.in.web.dto.request.CreateMusicRequest;
import whispy_server.whispy.domain.music.application.port.in.CreateMusicUseCase;
import whispy_server.whispy.domain.music.application.port.out.MusicSavePort;
import whispy_server.whispy.domain.music.model.Music;

/**
 * 음악 생성 서비스.
 *
 * 새로운 음악을 생성하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreateMusicService implements CreateMusicUseCase {

    private final MusicSavePort musicSavePort;

    /**
     * 음악을 생성하고 저장합니다.
     *
     * @param request 생성할 음악 정보가 포함된 요청
     */
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

