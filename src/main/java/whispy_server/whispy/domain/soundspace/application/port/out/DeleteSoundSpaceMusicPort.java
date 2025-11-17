package whispy_server.whispy.domain.soundspace.application.port.out;

public interface DeleteSoundSpaceMusicPort {
    void deleteByUserIdAndMusicId(Long userId, Long musicId);
    void deleteAllByMusicId(Long musicId);
    void deleteByUserId(Long userId);
}
