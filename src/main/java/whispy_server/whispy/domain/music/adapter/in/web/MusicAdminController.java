package whispy_server.whispy.domain.music.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import whispy_server.whispy.domain.music.adapter.in.web.dto.request.CreateMusicRequest;
import whispy_server.whispy.domain.music.adapter.in.web.dto.request.UpdateMusicRequest;
import whispy_server.whispy.domain.music.application.port.in.CreateMusicUseCase;
import whispy_server.whispy.domain.music.application.port.in.UpdateMusicUseCase;
import whispy_server.whispy.domain.music.application.port.in.DeleteMusicUseCase;

@RestController
@RequestMapping("/api/admin/musics")
@RequiredArgsConstructor
public class MusicAdminController {

    private final CreateMusicUseCase createMusicUseCase;
    private final UpdateMusicUseCase updateMusicUseCase;
    private final DeleteMusicUseCase deleteMusicUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createMusic(@RequestBody CreateMusicRequest request) {
        createMusicUseCase.execute(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateMusic(
            @PathVariable Long id,
            @RequestBody UpdateMusicRequest request) {
        updateMusicUseCase.execute(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMusic(@PathVariable Long id) {
        deleteMusicUseCase.execute(id);
    }
}
