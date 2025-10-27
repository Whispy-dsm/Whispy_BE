package whispy_server.whispy.domain.focussession.application.port.out;

import whispy_server.whispy.domain.focussession.model.FocusSession;

public interface FocusSessionSavePort {
    FocusSession save(FocusSession session);
}
