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

/**
 * 집중 세션 삭제 서비스.
 *
 * 특정 집중 세션을 삭제하는 유스케이스 구현입니다.
 * 현재 사용자의 세션만 삭제 가능합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DeleteFocusSessionService implements DeleteFocusSessionUseCase {

    private final QueryFocusSessionPort queryFocusSessionPort;
    private final DeleteFocusSessionPort deleteFocusSessionPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 특정 집중 세션을 삭제합니다.
     *
     * @param focusSessionId 삭제할 집중 세션 ID
     * @throws FocusSessionNotFoundException 해당 ID의 세션이 없거나 현재 사용자의 세션이 아닐 경우
     */
    @Override
    public void execute(Long focusSessionId) {
        Long userId = userFacadeUseCase.currentUser().id();
        queryFocusSessionPort.findByIdAndUserId(focusSessionId, userId)
                .orElseThrow(() -> FocusSessionNotFoundException.EXCEPTION);

        deleteFocusSessionPort.deleteById(focusSessionId);
    }
}