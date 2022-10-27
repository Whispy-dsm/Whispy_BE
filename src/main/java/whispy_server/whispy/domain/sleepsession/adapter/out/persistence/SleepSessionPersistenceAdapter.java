package whispy_server.whispy.domain.sleepsession.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.sleepsession.adapter.out.mapper.SleepSessionMapper;
import whispy_server.whispy.domain.sleepsession.adapter.out.persistence.repository.SleepSessionRepository;
import whispy_server.whispy.domain.sleepsession.application.port.out.SleepSessionSavePort;
import whispy_server.whispy.domain.sleepsession.model.SleepSession;

@Component
@RequiredArgsConstructor
public class SleepSessionPersistenceAdapter implements SleepSessionSavePort {

    private final SleepSessionRepository sleepSessionRepository;
    private final SleepSessionMapper sleepSessionMapper;

    @Override
    public SleepSession save(SleepSession sleepSession) {
        return sleepSessionMapper.toModel(
                sleepSessionRepository.save(
                        sleepSessionMapper.toEntity(sleepSession)
                )
        );
    }
}
