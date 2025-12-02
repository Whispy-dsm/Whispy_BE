package whispy_server.whispy.domain.soundspace.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.soundspace.adapter.in.web.dto.response.MusicInSoundSpaceCheckResponse;
import whispy_server.whispy.domain.soundspace.adapter.in.web.dto.response.SoundSpaceMusicResponse;
import whispy_server.whispy.domain.soundspace.application.port.in.CheckMusicInSoundSpaceUseCase;
import whispy_server.whispy.domain.soundspace.application.port.in.GetSoundSpaceMusicsUseCase;
import whispy_server.whispy.domain.soundspace.application.port.in.RemoveMusicFromSoundSpaceUseCase;
import whispy_server.whispy.domain.soundspace.application.port.in.ToggleSoundSpaceMusicUseCase;
import whispy_server.whispy.global.document.api.soundspace.SoundSpaceApiDocument;

import java.util.List;

/**
 * 사운드스페이스 REST 컨트롤러.
 *
 * 사용자 커스텀 음악 컬렉션 관리 기능을 제공하는 인바운드 어댑터입니다.
 */
@RestController
@RequestMapping("/soundspace")
@RequiredArgsConstructor
public class SoundSpaceController implements SoundSpaceApiDocument {

    private final GetSoundSpaceMusicsUseCase getSoundSpaceMusicsUseCase;
    private final ToggleSoundSpaceMusicUseCase toggleSoundSpaceMusicUseCase;
    private final CheckMusicInSoundSpaceUseCase checkMusicInSoundSpaceUseCase;
    private final RemoveMusicFromSoundSpaceUseCase removeMusicFromSoundSpaceUseCase;

    /**
     * 사운드스페이스 음악 목록을 조회합니다.
     *
     * @return 사운드스페이스 음악 목록
     */
    @Override
    @GetMapping("/music")
    public List<SoundSpaceMusicResponse> getSoundSpaceMusics() {
        return getSoundSpaceMusicsUseCase.execute();
    }

    /**
     * 사운드스페이스에 음악을 토글합니다 (추가/제거).
     *
     * @param musicId 음악 ID
     */
    @Override
    @PostMapping("/toggle/{musicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void toggleSoundSpaceMusic(@PathVariable Long musicId) {
        toggleSoundSpaceMusicUseCase.execute(musicId);
    }

    /**
     * 음악이 사운드스페이스에 있는지 확인합니다.
     *
     * @param musicId 음악 ID
     * @return 사운드스페이스 포함 여부 응답
     */
    @Override
    @GetMapping("/exists/{musicId}")
    public MusicInSoundSpaceCheckResponse checkMusicInSoundSpace(@PathVariable Long musicId) {
        return checkMusicInSoundSpaceUseCase.execute(musicId);
    }

    /**
     * 사운드스페이스에서 음악을 제거합니다.
     *
     * @param musicId 음악 ID
     */
    @Override
    @DeleteMapping("/{musicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMusicFromSoundSpace(@PathVariable Long musicId) {
        removeMusicFromSoundSpaceUseCase.execute(musicId);
    }
}
