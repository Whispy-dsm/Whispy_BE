package whispy_server.whispy.domain.meditationsession.application.port.out;

import whispy_server.whispy.domain.meditationsession.model.MeditationSession;

public interface MeditationSessionSavePort {
    MeditationSession save(MeditationSession session);
}
