package whispy_server.whispy.domain.sleepsession.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.sleepsession.adapter.out.mapper.SleepSessionMapper;
import whispy_server.whispy.domain.sleepsession.adapter.out.persistence.repository.SleepSessionRepository;
import whispy_server.whispy.domain.sleepsession.application.port.out.SleepSessionPort;
import whispy_server.whispy.domain.sleepsession.model.SleepSession;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SleepSessionPersistenceAdapter implements SleepSessionPort {

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

    @Override
    public Page<SleepSession> findByUserId(Long userId, Pageable pageable) {
        return sleepSessionMapper.toModelPage(sleepSessionRepository.findByUserIdOrderByStartedAtDesc(userId, pageable));
    }

    @Override
    public Optional<SleepSession> findByIdAndUserId(Long id, Long userId) {
        return sleepSessionMapper.toOptionalModel(sleepSessionRepository.findByIdAndUserId(id, userId));
    }

    @Override
    public void deleteById(Long id) {
        sleepSessionRepository.deleteById(id);
    }
}
