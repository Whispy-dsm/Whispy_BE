package whispy_server.whispy.domain.like.application.port.in;

import whispy_server.whispy.domain.like.adapter.in.web.dto.response.LikedMusicResponse;
import whispy_server.whispy.global.annotation.UseCase;
import java.util.List;

/**
 * 현재 사용자가 좋아요한 음악 목록을 조회하는 유스케이스이다.
 */
@UseCase
public interface QueryMyLikedMusicsUseCase {
    /**
     * 좋아요 목록을 최신순으로 조회한다.
     *
     * @return 좋아요한 음악 응답 리스트
     */
    List<LikedMusicResponse> execute();
}
