package whispy_server.whispy.domain.focussession.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.request.SaveFocusSessionRequest;
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.response.FocusSessionResponse;
import whispy_server.whispy.domain.focussession.application.port.in.SaveFocusSessionUseCase;
import whispy_server.whispy.domain.focussession.application.port.out.FocusSessionSavePort;
import whispy_server.whispy.domain.focussession.model.FocusSession;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.application.port.out.QueryUserPort;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.exception.domain.user.UserNotFoundException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SaveFocusSessionService implements SaveFocusSessionUseCase {

    private final FocusSessionSavePort focusSessionSavePort;
    private final UserFacadeUseCase userFacadeUseCase;
    private final QueryUserPort queryUserPort;

    @Transactional
    @Override
    public FocusSessionResponse execute(SaveFocusSessionRequest request) {
        String email = userFacadeUseCase.currentUser().email();
        User user = queryUserPort.findByEmail(email)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        FocusSession focusSession = new FocusSession(
                null,
                user.id(),
                request.musicId(),
                request.startedAt(),
                request.endedAt(),
                request.durationSeconds(),
                request.tag(),
                LocalDateTime.now()
        );

        FocusSession saved = focusSessionSavePort.save(focusSession);

        return FocusSessionResponse.from(saved);
    }
}
