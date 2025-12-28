package whispy_server.whispy.domain.music.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.music.adapter.in.web.dto.response.MusicDetailResponse;
import whispy_server.whispy.domain.music.application.port.in.GetMusicDetailUseCase;
import whispy_server.whispy.domain.music.application.port.out.QueryMusicPort;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.global.annotation.UserAction;
import whispy_server.whispy.global.exception.domain.music.MusicNotFoundException;

/**
 * 음악 상세 조회 서비스.
 *
 * 지정된 ID의 음악 상세 정보를 조회하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class GetMusicDetailService implements GetMusicDetailUseCase {

    private final QueryMusicPort queryMusicPort;

    /**
     * 지정된 ID의 음악 상세 정보를 조회합니다.
     *
     * @param id 조회할 음악의 ID
     * @return 음악 상세 정보
     * @throws MusicNotFoundException 음악을 찾을 수 없을 때 발생
     */
    @UserAction("음악 상세 조회")
    @Transactional(readOnly = true)
    @Override
    public MusicDetailResponse execute(Long id) {
        Music music = queryMusicPort.findById(id)
                .orElseThrow(() -> MusicNotFoundException.EXCEPTION);

        return MusicDetailResponse.from(music);
    }
}
