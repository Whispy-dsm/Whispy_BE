package whispy_server.whispy.domain.music.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.music.adapter.in.web.dto.request.UpdateMusicRequest;
import whispy_server.whispy.domain.music.application.port.in.UpdateMusicUseCase;
import whispy_server.whispy.domain.music.application.port.out.MusicSavePort;
import whispy_server.whispy.domain.music.application.port.out.QueryMusicPort;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.global.config.redis.RedisConfig;
import whispy_server.whispy.global.annotation.AdminAction;
import whispy_server.whispy.global.exception.domain.music.MusicNotFoundException;

/**
 * 음악 수정 서비스.
 *
 * 기존 음악 정보를 수정하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateMusicService implements UpdateMusicUseCase {

    private final QueryMusicPort queryMusicPort;
    private final MusicSavePort musicSavePort;

    /**
     * 음악 정보를 수정합니다.
     *
     * @param request 수정할 음악 정보가 포함된 요청
     * @throws MusicNotFoundException 음악을 찾을 수 없을 때 발생
     */
    @AdminAction("음악 정보 수정")
    @Transactional
    @CacheEvict(value = RedisConfig.MUSIC_CATEGORY_SEARCH_CACHE, allEntries = true)
    @Override
    public void execute(UpdateMusicRequest request) {
        Music existingMusic = queryMusicPort.findById(request.id())
                .orElseThrow(() -> MusicNotFoundException.EXCEPTION);

        Music updatedMusic = existingMusic.update(
                request.title(),
                request.artist(),
                request.description(),
                request.filePath(),
                request.duration(),
                request.category(),
                request.bannerImageUrl(),
                request.videoUrl()
        );
        musicSavePort.save(updatedMusic);
    }
}
