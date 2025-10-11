package whispy_server.whispy.domain.soundspace.application.port.in;

public interface RemoveMusicFromSoundSpaceUseCase {
    void execute(Long musicId);
}
