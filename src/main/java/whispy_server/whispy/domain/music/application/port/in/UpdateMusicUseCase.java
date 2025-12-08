package whispy_server.whispy.domain.music.application.port.in;

import whispy_server.whispy.domain.music.adapter.in.web.dto.request.UpdateMusicRequest;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 음악 수정 유스케이스.
 *
 * 음악 정보를 수정하는 인바운드 포트입니다.
 */
@UseCase
public interface UpdateMusicUseCase {
    /**
     * 음악 정보를 수정합니다.
     *
     * @param request 수정할 음악 정보가 포함된 요청
     */
    void execute(UpdateMusicRequest request);
}
