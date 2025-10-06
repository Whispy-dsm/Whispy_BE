package whispy_server.whispy.domain.history.application.port.out;

import whispy_server.whispy.domain.history.model.ListeningHistory;

public interface SaveListeningHistoryPort {
    void save(ListeningHistory history);
}
