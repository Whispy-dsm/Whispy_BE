package whispy_server.whispy.domain.music.application.port.in;

import whispy_server.whispy.domain.music.adapter.in.web.dto.request.CreateMusicRequest;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 음악 생성 유스케이스.
 *
 * 새로운 음악을 생성하는 인바운드 포트입니다.
 */
@UseCase
public interface CreateMusicUseCase {
    /**
     * 음악을 생성합니다.
     *
     * @param request 생성할 음악 정보가 포함된 요청
     */
    void execute(CreateMusicRequest request);
}
