package whispy_server.whispy.domain.soundspace.application.port.out;

/**
 * 사운드 스페이스 엔트리를 삭제하는 포트.
 */
public interface DeleteSoundSpaceMusicPort {
    void deleteByUserIdAndMusicId(Long userId, Long musicId);
    void deleteAllByMusicId(Long musicId);
    void deleteByUserId(Long userId);
}
