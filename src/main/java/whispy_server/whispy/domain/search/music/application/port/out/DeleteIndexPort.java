package whispy_server.whispy.domain.search.music.application.port.out;

public interface DeleteIndexPort {
    void deleteFromIndex(Long musicId);
}
