package whispy_server.whispy.domain.meditationsession.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.meditationsession.adapter.out.mapper.MeditationSessionMapper;
import whispy_server.whispy.domain.meditationsession.adapter.out.persistence.repository.MeditationSessionRepository;
import whispy_server.whispy.domain.meditationsession.application.port.out.MeditationSessionPort;
import whispy_server.whispy.domain.meditationsession.model.MeditationSession;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MeditationSessionPersistenceAdapter implements MeditationSessionPort {

    private final MeditationSessionRepository meditationSessionRepository;
    private final MeditationSessionMapper meditationSessionMapper;

    @Override
    public MeditationSession save(MeditationSession meditationSession) {
        return meditationSessionMapper.toModel(
                meditationSessionRepository.save(
                        meditationSessionMapper.toEntity(meditationSession)
                )
        );
    }

    @Override
    public Page<MeditationSession> findByUserId(Long userId, Pageable pageable) {
        return meditationSessionMapper.toModelPage(meditationSessionRepository.findByUserIdOrderByStartedAtDesc(userId, pageable));
    }

    @Override
    public Optional<MeditationSession> findByIdAndUserId(Long id, Long userId) {
        return meditationSessionMapper.toOptionalModel(meditationSessionRepository.findByIdAndUserId(id, userId));
    }

    @Override
    public void deleteById(Long id) {
        meditationSessionRepository.deleteById(id);
    }
}
