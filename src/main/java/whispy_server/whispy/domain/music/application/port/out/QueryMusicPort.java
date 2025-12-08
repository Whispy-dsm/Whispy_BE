package whispy_server.whispy.domain.music.application.port.out;

import whispy_server.whispy.domain.music.model.Music;
import java.util.Optional;

/**
 * 음악 조회 아웃바운드 포트.
 *
 * 음악 조회 기능을 정의하는 아웃바운드 포트입니다.
 */
public interface QueryMusicPort {
    /**
     * 지정된 ID의 음악을 조회합니다.
     *
     * @param id 조회할 음악의 ID
     * @return 음악 도메인 모델을 포함하는 Optional
     */
    Optional<Music> findById(Long id);

    /**
     * 지정된 ID의 음악이 존재하는지 확인합니다.
     *
     * @param id 확인할 음악의 ID
     * @return 음악이 존재하면 true, 그렇지 않으면 false
     */
    boolean existsById(Long id);
}
