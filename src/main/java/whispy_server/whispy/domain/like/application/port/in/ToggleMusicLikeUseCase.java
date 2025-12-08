package whispy_server.whispy.domain.like.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

/**
 * 음악 좋아요 상태를 토글하는 유스케이스 인터페이스.
 */
@UseCase
public interface ToggleMusicLikeUseCase {
    /**
     * 지정된 음악의 좋아요 여부를 반전한다.
     *
     * @param musicId 좋아요를 토글할 음악 ID
     */
    void execute(Long musicId);
}
