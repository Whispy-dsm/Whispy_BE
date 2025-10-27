package whispy_server.whispy.domain.focussession.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.focussession.adapter.out.mapper.FocusSessionMapper;
import whispy_server.whispy.domain.focussession.adapter.out.persistence.repository.FocusSessionRepository;
import whispy_server.whispy.domain.focussession.application.port.out.FocusSessionSavePort;
import whispy_server.whispy.domain.focussession.model.FocusSession;

@Component
@RequiredArgsConstructor
public class FocusSessionPersistenceAdapter implements FocusSessionSavePort {

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
}
