package whispy_server.whispy.domain.like.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import whispy_server.whispy.domain.like.adapter.in.web.dto.response.LikedMusicResponse;
import whispy_server.whispy.domain.like.application.port.in.QueryMyLikedMusicsUseCase;
import whispy_server.whispy.domain.like.application.port.in.ToggleMusicLikeUseCase;
import whispy_server.whispy.global.document.api.like.MusicLikeApiDocument;

import java.util.List;

@RestController
@RequestMapping("/music-likes")
@RequiredArgsConstructor
public class MusicLikeController implements MusicLikeApiDocument {

    private final ToggleMusicLikeUseCase toggleMusicLikeUseCase;
    private final QueryMyLikedMusicsUseCase queryMyLikedMusicsUseCase;

    @PostMapping("/{musicId}/toggle")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void toggleMusicLike(@PathVariable Long musicId) {
        toggleMusicLikeUseCase.execute(musicId);
    }

    @GetMapping("/my")
    public List<LikedMusicResponse> getMyLikedMusics() {
        return queryMyLikedMusicsUseCase.execute();
    }
}
