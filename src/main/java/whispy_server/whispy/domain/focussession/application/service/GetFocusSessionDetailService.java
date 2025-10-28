package whispy_server.whispy.domain.focussession.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.response.FocusSessionDetailResponse;
import whispy_server.whispy.domain.focussession.application.port.in.GetFocusSessionDetailUseCase;
import whispy_server.whispy.domain.focussession.application.port.out.QueryFocusSessionPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.global.exception.domain.focussession.FocusSessionNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetFocusSessionDetailService implements GetFocusSessionDetailUseCase {

    private final QueryFocusSessionPort queryFocusSessionPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public FocusSessionDetailResponse execute(Long focusSessionId) {
        Long userId = userFacadeUseCase.currentUser().id();
        return queryFocusSessionPort.findByIdAndUserId(userId, focusSessionId)
                .map(FocusSessionDetailResponse::from)
                .orElseThrow(() -> FocusSessionNotFoundException.EXCEPTION);
    }
}
