package whispy_server.whispy.domain.soundspace.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

/**
 * 사운드 스페이스에 음악을 추가/삭제하는 토글 유스케이스 정의.
 */
@UseCase
public interface ToggleSoundSpaceMusicUseCase {
    void execute(Long musicId);
}
