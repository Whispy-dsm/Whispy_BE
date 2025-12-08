package whispy_server.whispy.domain.music.application.port.in;

import whispy_server.whispy.domain.music.adapter.in.web.dto.response.MusicDetailResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 음악 상세 조회 유스케이스.
 *
 * 지정된 ID의 음악 상세 정보를 조회하는 인바운드 포트입니다.
 */
@UseCase
public interface GetMusicDetailUseCase {
    /**
     * 지정된 ID의 음악 상세 정보를 조회합니다.
     *
     * @param id 조회할 음악의 ID
     * @return 음악 상세 정보
     */
    MusicDetailResponse execute(Long id);
}
