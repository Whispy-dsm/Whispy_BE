package whispy_server.whispy.domain.music.application.port.out;

/**
 * 음악 삭제 아웃바운드 포트.
 *
 * 음악 삭제 기능을 정의하는 아웃바운드 포트입니다.
 */
public interface MusicDeletePort {
    /**
     * 지정된 ID의 음악을 삭제합니다.
     *
     * @param id 삭제할 음악의 ID
     */
    void deleteById(Long id);
}
