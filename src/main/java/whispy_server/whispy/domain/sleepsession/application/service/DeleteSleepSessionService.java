package whispy_server.whispy.domain.sleepsession.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.sleepsession.application.port.in.DeleteSleepSessionUseCase;
import whispy_server.whispy.domain.sleepsession.application.port.out.DeleteSleepSessionPort;
import whispy_server.whispy.domain.sleepsession.application.port.out.QuerySleepSessionPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.global.exception.domain.focussession.FocusSessionNotFoundException;

/**
 * 수면 세션 삭제 서비스.
 *
 * 특정 수면 세션을 삭제하는 유스케이스 구현입니다.
 * 현재 사용자의 세션만 삭제 가능합니다.
 */
@Service
@RequiredArgsConstructor
public class DeleteSleepSessionService implements DeleteSleepSessionUseCase {

    private final QuerySleepSessionPort querySleepSessionPort;
    private final DeleteSleepSessionPort deleteSleepSessionPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 특정 수면 세션을 삭제합니다.
     *
     * @param focusSessionId 삭제할 수면 세션 ID
     * @throws FocusSessionNotFoundException 해당 ID의 세션이 없거나 현재 사용자의 세션이 아닐 경우
     */
    @Override
    public void execute(Long focusSessionId) {
        Long userId = userFacadeUseCase.currentUser().id();
        querySleepSessionPort.findByIdAndUserId(focusSessionId, userId)
                .orElseThrow(() -> FocusSessionNotFoundException.EXCEPTION);

        deleteSleepSessionPort.deleteById(focusSessionId);
    }
}
