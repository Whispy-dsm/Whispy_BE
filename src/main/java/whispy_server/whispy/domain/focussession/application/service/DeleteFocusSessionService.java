// DeleteFocusSessionService.java
package whispy_server.whispy.domain.focussession.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.focussession.application.port.in.DeleteFocusSessionUseCase;
import whispy_server.whispy.domain.focussession.application.port.out.DeleteFocusSessionPort;
import whispy_server.whispy.domain.focussession.application.port.out.QueryFocusSessionPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.global.exception.domain.focussession.FocusSessionNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteFocusSessionService implements DeleteFocusSessionUseCase {

    private final QueryFocusSessionPort queryFocusSessionPort;
    private final DeleteFocusSessionPort deleteFocusSessionPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public void execute(Long focusSessionId) {
        Long userId = userFacadeUseCase.currentUser().id();
        queryFocusSessionPort.findByIdAndUserId(focusSessionId, userId)
                .orElseThrow(() -> FocusSessionNotFoundException.EXCEPTION);

        deleteFocusSessionPort.deleteById(focusSessionId);
    }
}