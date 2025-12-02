package whispy_server.whispy.domain.music.application.port.out;

import whispy_server.whispy.domain.music.model.Music;

/**
 * 음악 저장 아웃바운드 포트.
 *
 * 음악 저장 기능을 정의하는 아웃바운드 포트입니다.
 */
public interface MusicSavePort {
    /**
     * 음악을 저장합니다.
     *
     * @param music 저장할 음악 도메인 모델
     * @return 저장된 음악 도메인 모델
     */
    Music save(Music music);
}
