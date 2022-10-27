package whispy_server.whispy.domain.sleepsession.application.port.out;

import whispy_server.whispy.domain.sleepsession.model.SleepSession;

public interface SleepSessionSavePort {
    SleepSession save(SleepSession session);
}
