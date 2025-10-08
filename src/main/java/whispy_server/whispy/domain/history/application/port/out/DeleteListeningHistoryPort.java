package whispy_server.whispy.domain.history.application.port.out;

public interface DeleteListeningHistoryPort {
    void deleteAllByMusicId(Long musicId);
}
