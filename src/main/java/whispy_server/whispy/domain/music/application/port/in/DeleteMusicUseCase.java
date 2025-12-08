package whispy_server.whispy.domain.music.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

/**
 * 음악 삭제 유스케이스.
 *
 * 음악을 삭제하는 인바운드 포트입니다.
 */
@UseCase
public interface DeleteMusicUseCase {
    /**
     * 지정된 ID의 음악을 삭제합니다.
     *
     * @param id 삭제할 음악의 ID
     */
    void execute(Long id);
}
