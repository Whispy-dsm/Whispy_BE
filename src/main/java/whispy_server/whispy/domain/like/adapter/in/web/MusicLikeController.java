package whispy_server.whispy.domain.like.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import whispy_server.whispy.domain.like.adapter.in.web.dto.response.CheckMusicLikeResponse;
import whispy_server.whispy.domain.like.adapter.in.web.dto.response.LikedMusicResponse;
import whispy_server.whispy.domain.like.application.port.in.CheckMusicLikeUseCase;
import whispy_server.whispy.domain.like.application.port.in.QueryMyLikedMusicsUseCase;
import whispy_server.whispy.domain.like.application.port.in.ToggleMusicLikeUseCase;
import whispy_server.whispy.global.document.api.like.MusicLikeApiDocument;

import java.util.List;

/**
 * 음악 좋아요 REST 컨트롤러.
 *
 * 음악 좋아요 토글 및 조회 기능을 제공하는 인바운드 어댑터입니다.
 */
@RestController
@RequestMapping("/music-likes")
@RequiredArgsConstructor
public class MusicLikeController implements MusicLikeApiDocument {

    private final ToggleMusicLikeUseCase toggleMusicLikeUseCase;
    private final QueryMyLikedMusicsUseCase queryMyLikedMusicsUseCase;
    private final CheckMusicLikeUseCase checkMusicLikeUseCase;

    /**
     * 음악 좋아요를 토글합니다 (추가/제거).
     *
     * @param musicId 음악 ID
     */
    @PostMapping("/{musicId}/toggle")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void toggleMusicLike(@PathVariable Long musicId) {
        toggleMusicLikeUseCase.execute(musicId);
    }

    /**
     * 내가 좋아요한 음악 목록을 조회합니다.
     *
     * @return 좋아요한 음악 목록
     */
    @GetMapping("/my")
    public List<LikedMusicResponse> getMyLikedMusics() {
        return queryMyLikedMusicsUseCase.execute();
    }

    /**
     * 특정 음악에 대한 좋아요 여부를 확인합니다.
     *
     * @param musicId 음악 ID
     * @return 좋아요 여부 응답
     */
    @GetMapping("/{musicId}/check")
    public CheckMusicLikeResponse checkMusicLike(@PathVariable Long musicId) {
        return checkMusicLikeUseCase.execute(musicId);
    }
}
