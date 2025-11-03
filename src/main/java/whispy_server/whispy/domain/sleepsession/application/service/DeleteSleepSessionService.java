package whispy_server.whispy.domain.sleepsession.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.sleepsession.application.port.in.DeleteSleepSessionUseCase;
import whispy_server.whispy.domain.sleepsession.application.port.out.DeleteSleepSessionPort;
import whispy_server.whispy.domain.sleepsession.application.port.out.QuerySleepSessionPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.global.exception.domain.focussession.FocusSessionNotFoundException;

@Service
@RequiredArgsConstructor
public class DeleteSleepSessionService implements DeleteSleepSessionUseCase {

    private final QuerySleepSessionPort querySleepSessionPort;
    private final DeleteSleepSessionPort deleteSleepSessionPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public void execute(Long focusSessionId) {
        Long userId = userFacadeUseCase.currentUser().id();
        querySleepSessionPort.findByIdAndUserId(focusSessionId, userId)
                .orElseThrow(() -> FocusSessionNotFoundException.EXCEPTION);

        deleteSleepSessionPort.deleteById(focusSessionId);
    }
}
