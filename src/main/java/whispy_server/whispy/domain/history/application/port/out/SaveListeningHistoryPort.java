package whispy_server.whispy.domain.history.application.port.out;

import whispy_server.whispy.domain.history.model.ListeningHistory;

/**
 * 청취 이력 저장 Port.
 */
public interface SaveListeningHistoryPort {
    /**
     * 청취 이력을 저장한다.
     */
    void save(ListeningHistory history);
}
