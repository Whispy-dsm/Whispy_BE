package whispy_server.whispy.domain.focussession.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.focussession.adapter.out.mapper.FocusSessionMapper;
import whispy_server.whispy.domain.focussession.adapter.out.persistence.repository.FocusSessionRepository;
import whispy_server.whispy.domain.focussession.application.port.out.FocusSessionPort;
import whispy_server.whispy.domain.focussession.model.FocusSession;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FocusSessionPersistenceAdapter implements FocusSessionPort {

    private final FocusSessionRepository focusSessionRepository;
    private final FocusSessionMapper focusSessionMapper;

    @Override
    public FocusSession save(FocusSession focusSession) {
        return focusSessionMapper.toModel(
                focusSessionRepository.save(
                        focusSessionMapper.toEntity(focusSession)
                )
        );
    }

    @Override
    public Page<FocusSession> findByUserId(Long userId, Pageable pageable) {
        return focusSessionMapper.toModelPage(focusSessionRepository.findByUserIdOrderByStartedAtDesc(userId, pageable));
    }

    @Override
    public Optional<FocusSession> findByIdAndUserId(Long id, Long userId) {
        return focusSessionMapper.toOptionalModel(focusSessionRepository.findByIdAndUserId(id, userId));
    }

    @Override
    public void deleteById(Long id) {
        focusSessionRepository.deleteById(id);
    }
}
