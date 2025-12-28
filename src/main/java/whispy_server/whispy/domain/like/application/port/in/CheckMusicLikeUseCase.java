package whispy_server.whispy.domain.like.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

/**
 * 특정 음악에 대한 좋아요 여부를 확인하는 유스케이스이다.
 */
@UseCase
public interface CheckMusicLikeUseCase {
    /**
     * 현재 사용자가 특정 음악에 좋아요를 눌렀는지 확인한다.
     *
     * @param musicId 확인할 음악 ID
     * @return 좋아요 여부 (true: 좋아요 누름, false: 좋아요 안 누름)
     */
    boolean execute(Long musicId);
}
