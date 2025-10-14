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

@RestController
@RequestMapping("/soundspace")
@RequiredArgsConstructor
public class SoundSpaceController implements SoundSpaceApiDocument {

    private final GetSoundSpaceMusicsUseCase getSoundSpaceMusicsUseCase;
    private final ToggleSoundSpaceMusicUseCase toggleSoundSpaceMusicUseCase;
    private final CheckMusicInSoundSpaceUseCase checkMusicInSoundSpaceUseCase;
    private final RemoveMusicFromSoundSpaceUseCase removeMusicFromSoundSpaceUseCase;

    @Override
    @GetMapping("/music")
    public List<SoundSpaceMusicResponse> getSoundSpaceMusics() {
        return getSoundSpaceMusicsUseCase.execute();
    }

    @Override
    @PostMapping("/toggle/{musicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void toggleSoundSpaceMusic(@PathVariable Long musicId) {
        toggleSoundSpaceMusicUseCase.execute(musicId);
    }

    @Override
    @GetMapping("/exists/{musicId}")
    public MusicInSoundSpaceCheckResponse checkMusicInSoundSpace(@PathVariable Long musicId) {
        return checkMusicInSoundSpaceUseCase.execute(musicId);
    }

    @Override
    @DeleteMapping("/{musicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMusicFromSoundSpace(@PathVariable Long musicId) {
        removeMusicFromSoundSpaceUseCase.execute(musicId);
    }
}
